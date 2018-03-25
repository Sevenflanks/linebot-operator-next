package next.operator.scheduled;

import lombok.extern.slf4j.Slf4j;
import next.operator.subscription.dao.SubscriptionDao;
import next.operator.subscription.entity.Subscription;
import next.operator.subscription.service.SubscriptionService;
import org.ocpsoft.prettytime.PrettyTime;
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
import java.util.List;
import java.util.Locale;

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
    log.info("subscribing {}'s job, start:{}, fix:{}, msg:{}, to:{}",
        subscription.getSubscriber().getSubscriberName(),
        subscription.getStartTime(),
        subscription.getFixedRate(),
        subscription.getMsg(),
        subscription.getSubscriber().getSubscribeTo());

    // 下次應執行時間
    final Instant next = toNext(subscription.getStartTime(), subscription.getFixedRate());

    //　開始進行排程註冊
    subscription.setStartTime(next);
    taskScheduler.scheduleAtFixedRate(() -> subscriptionService.push(subscription.getId()),
        Date.from(subscription.getStartTime()),
        subscription.getFixedRate().toMillis()
    );
    subscriptionDao.save(subscription);

    // 上次應執行時間
    final Instant prev = next.minus(subscription.getFixedRate());
    // 若不存在上次執行時間，代表是第一次進行訂閱
    if (subscription.getLastPushTime() == null) {
      log.info("ID:{}) First time subscribe, show detail", subscription.getId());
      subscriptionService.push(subscription.getId(), "這是你剛剛訂閱的訊息:" + subscription + "\n");
    }
    // 當上次應執行時間 > 上次執行時間，代表上一次應執行時間時並沒有執行，此時要補執行
    else if (prev.isAfter(subscription.getLastPushTime())) {
      log.info("ID:{}) is delay, send subscription immediately", subscription.getId());
      subscriptionService.push(subscription.getId(),
          "歹勢，剛剛睡著了啦，這是原本應該要在" +
              new PrettyTime(Locale.TRADITIONAL_CHINESE).format(Date.from(prev)).replaceAll(" ", "") +
              "發的訊息\n"
      );
    }
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
