package next.operator.scheduled;

import lombok.extern.slf4j.Slf4j;
import next.operator.subscription.dao.SubscriptionDao;
import next.operator.subscription.entity.Subscription;
import next.operator.subscription.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 訂閱訊息動態註冊
 */
@Slf4j
@Component
@Order(3)
public class SubscriptionJob {

  @Autowired
  private TaskScheduler taskScheduler;

  @Autowired
  private SubscriptionDao subscriptionDao;

  @Autowired
  private SubscriptionService subscriptionService;

  /**
   * 由於目前伺服器會有待機狀態, 醒來後要檢查
   * 1. 是否有已經過期的訊息尚未發送
   * 2. 註冊排程
   */
  @EventListener
  @Transactional
  public void subscribe(ContextRefreshedEvent event) throws InterruptedException {
    final List<Subscription> subscriptions = subscriptionDao.findAll();

    log.info("subscribing jobs, size:{}", subscriptions.size());

    for (Subscription subscription : subscriptions) {
      subscribe(subscription);
    }
    log.info("subscribing job success");
  }

  @Transactional
  public void subscribe(Subscription subscription) {
    final Instant next = toNext(subscription.getStartTime(), subscription.getFixedRate());

    //　開始進行排程註冊
    subscription.setStartTime(next);
    taskScheduler.scheduleAtFixedRate(() -> subscriptionService.push(subscription.getId()),
        Date.from(subscription.getStartTime()),
        subscription.getFixedRate().toMillis()
    );
    subscriptionDao.save(subscription);

    // 判斷上一次的排程有沒有執行過(六十秒緩衝判斷)
    final Instant prev = next.minus(subscription.getFixedRate());
    if (subscription.getLastPushTime() == null) {
      subscriptionService.push(subscription.getId(), "這是你剛剛訂閱的訊息:" + subscription + "\n");
    } else if (subscription.getStartTime().isBefore(Instant.now()) && subscription.getLastPushTime().plusSeconds(60).isBefore(prev)) {
      subscriptionService.push(subscription.getId(),
          "拍謝，剛剛睡著了啦，這是原本應該要在" +
              DateTimeFormatter.ISO_ZONED_DATE_TIME.format(prev.atOffset(ZoneOffset.ofHours(8))) +
              "發的訊息\n"
      );
    }

    log.info("subscribed {}'s job, start:{}, fix:{}, msg:{}, to:{}",
        subscription.getSubscriber().getSubscriberName(),
        subscription.getStartTime(),
        subscription.getFixedRate(),
        subscription.getMsg(),
        subscription.getSubscriber().getSubscribeTo());
  }

  public static Instant toNext(Instant time, Duration d) {
    log.debug("calc next time: {}, {}", time, d);
    Instant next = time;
    while (Instant.now().isAfter(next)) {
      next = next.plus(d);
    }
    return next;
  }

}
