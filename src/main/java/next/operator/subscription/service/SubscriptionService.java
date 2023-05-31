package next.operator.subscription.service;

import com.google.common.collect.Sets;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.Source;
import com.linecorp.bot.model.message.TextMessage;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import next.operator.common.validation.ValidationException;
import next.operator.common.validation.ValidationUtils;
import next.operator.linebot.service.RespondentService;
import next.operator.subscription.dao.SubscriptionDao;
import next.operator.subscription.entity.Subscriber;
import next.operator.subscription.entity.Subscription;
import next.operator.user.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Service
public class SubscriptionService {

  @Autowired private SubscriptionDao subscriptionDao;

  @Autowired private UserDao userDao;

  @Autowired private Validator validator;

  @Autowired private LineMessagingClient client;

  @Autowired private RespondentService respondentService;

  @Transactional
  public void deSubscribe(Source source) {
    Optional.ofNullable(subscriptionDao.findBySubscriber_SubscriberId(source.getUserId()))
        .ifPresent(s -> subscriptionDao.deleteAll(s));
  }

  @Transactional
  public void deSubscribe(Source source, Long id) {
    final Subscription subscription =
        subscriptionDao.findById(id)
            .orElseThrow(() -> new ValidationException("ID:" + id + ")這一則訂閱不存在喔！"));
    subscriptionDao.delete(subscription);
    if (!subscription.getSubscriber().getSubscriberId().equals(source.getUserId())) {
      throw new ValidationException("這不是你訂閱的東西喔！");
    }
  }

  @Transactional
  public Subscription subscribe(Source source, Instant startTime, Duration fixRate, String msg) {
    final Subscription subscription = new Subscription();
    final Subscriber subscriber = new Subscriber();

    subscriber.setSubscriberId(source.getUserId());
    subscriber.setSubscriberName(userDao.getCurrentUserName());
    subscriber.setSubscribeTo(source.getSenderId());

    subscription.setSubscriber(subscriber);
    subscription.setStartTime(startTime);
    subscription.setFixedRate(fixRate);
    subscription.setMsg(msg);

    return subscribe(subscription);
  }

  @Transactional
  public Subscription subscribe(Subscription subscription) {

    // 檢查是否有此人註冊過的消息
    final List<Subscription> dbSubscriptions =
        subscriptionDao.findBySubscriber_SubscriberId(
            subscription.getSubscriber().getSubscriberId());
    if (dbSubscriptions.size() >= 3) {
      throw new ValidationException(
          subscription.getSubscriber().getSubscriberName() + "不能再訂閱囉，目前每個人只能訂閱三個消息！");
    }

    // 欄位檢核
    final Stream<String> subscriptionMsgs =
        validator.validate(subscription).stream().map(ValidationUtils::from);
    final Stream<String> subscriberMsgs =
        Optional.ofNullable(subscription.getSubscriber())
            .map(s -> validator.validate(s))
            .orElse(Sets.newHashSet())
            .stream()
            .map(ValidationUtils::from);
    ValidationUtils.requiredMsgsEmpty(subscriptionMsgs, subscriberMsgs);

    log.info("subscribed Subscription:{}", subscription);
    return subscriptionDao.save(subscription);
  }

  @Transactional
  public void push(Long id) {
    push(id, null);
  }

  @Transactional
  public void push(Long id, String prefix) {
    log.info("pushing subscription, ID:{}", id);
    final Subscription subscription = subscriptionDao.findById(id)
        .orElseThrow(() -> new ValidationException("ID:" + id + ")這一則訂閱不存在喔！"));;

    final String response;
    // 如果是指令型，則自動觸發指令
    if (RespondentService.isCommand(subscription.getMsg())) {
      // 產生一個假的event
      final MessageEvent<TextMessageContent> event =
          MessageEvent.<TextMessageContent>builder()
              .source(new Source() {
                @Override
                public String getUserId() {
                  return subscription.getSubscriber().getSubscriberId();
                }

                @Override
                public String getSenderId() {
                  return subscription.getSubscriber().getSubscribeTo();
                }
              })
              .message(TextMessageContent.builder().text(subscription.getMsg()).build())
              .timestamp(Instant.now())
              .build();

      response =
          Optional.ofNullable(respondentService.response(event))
              .map(TextMessage::getText)
              .orElse(subscription.getMsg());
    } else {
      response = subscription.getMsg();
    }

    final String message =
        Optional.ofNullable(prefix).orElse("")
            + "以下是"
            + subscription.getSubscriber().getSubscriberName()
            + "訂閱的消息\n"
            + response;
    client.pushMessage(
        new PushMessage(subscription.getSubscriber().getSubscribeTo(), new TextMessage(message)));
    subscription.setLastPushTime(Instant.now());
    subscriptionDao.save(subscription);
  }
}
