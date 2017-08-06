package next.operator.subscription.service;

import com.google.common.collect.Sets;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.event.source.Source;
import com.linecorp.bot.model.message.TextMessage;
import lombok.extern.slf4j.Slf4j;
import next.operator.common.validation.ValidationUtils;
import next.operator.subscription.dao.SubscriptionDao;
import next.operator.subscription.entity.Subscriber;
import next.operator.subscription.entity.Subscription;
import next.operator.user.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import javax.validation.Validator;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Service
public class SubscriptionService {

  @Autowired
  private SubscriptionDao subscriptionDao;

  @Autowired
  private UserDao userDao;

  @Autowired
  private Validator validator;

  @Autowired
  private LineMessagingClient client;

  @Transactional
  public void deSubscribe(Source source) {
    Optional.ofNullable(subscriptionDao.findBySubscriber_SubscriberId(source.getUserId()))
        .ifPresent(subscriptionDao::delete);
  }

  @Transactional
  public Subscription subscribe(Source source, Instant startTime, Duration fixRate, String msg) {
    final Subscription subscription = new Subscription();
    final Subscriber subscriber = new Subscriber();

    subscriber.setSubscriberId(source.getSenderId());
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
    final Subscription dbSubscription = subscriptionDao.findBySubscriber_SubscriberId(subscription.getSubscriber().getSubscriberId());
    if (dbSubscription != null) {
      throw new ValidationException(subscription.getSubscriber().getSubscriberName() + "已經有訂閱的消息，目前每人只能訂閱一種消息喔！");
    }

    // 欄位檢核
    final Stream<String> subscriptionMsgs = validator.validate(subscription).stream()
        .map(ValidationUtils::from);
    final Stream<String> subscriberMsgs = Optional.ofNullable(subscription.getSubscriber())
        .map(s -> validator.validate(s))
        .orElse(Sets.newHashSet()).stream()
        .map(ValidationUtils::from);
    ValidationUtils.requiredMsgsEmpty(subscriptionMsgs, subscriberMsgs);

    return subscriptionDao.save(subscription);
  }

  @Transactional
  public void push(Long id) {
    final Subscription subscription = subscriptionDao.findOne(id);
    final String message = "以下是" + subscription.getSubscriber().getSubscriberName() + "訂閱的消息";
    // TODO
    client.pushMessage(new PushMessage(subscription.getSubscriber().getSubscribeTo(), new TextMessage(message)));
    subscription.setLastPushTime(Instant.now());
    subscriptionDao.save(subscription);
  }

}
