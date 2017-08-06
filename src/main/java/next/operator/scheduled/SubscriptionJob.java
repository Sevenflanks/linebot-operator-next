package next.operator.scheduled;

import lombok.extern.slf4j.Slf4j;
import next.operator.subscription.dao.SubscriptionDao;
import next.operator.subscription.entity.Subscription;
import next.operator.subscription.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.sql.Date;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

/**
 * 訂閱訊息動態註冊
 */
@Slf4j
@Component
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
  @PostConstruct
  @Transactional
  public void subscribe() {
    final List<Subscription> subscriptions = subscriptionDao.findAll();

    log.info("subscribing jobs, size:{}", subscriptions.size());

    for (Subscription subscription : subscriptions) {
      subscribe(subscription);
    }
  }

  @Transactional
  public void subscribe(Subscription subscription) {
    final Instant next = toNext(subscription.getStartTime(), subscription.getFixedRate());
    subscription.setStartTime(next);

    log.info("subscribing {}'s job, start:{}, fix:{}, msg:{}, to:{}",
        subscription.getSubscriber().getSubscriberName(),
        subscription.getStartTime(),
        subscription.getFixedRate(),
        subscription.getMsg(),
        subscription.getSubscriber().getSubscribeTo());

    taskScheduler.scheduleAtFixedRate(() -> subscriptionService.push(subscription.getId()),
        Date.from(subscription.getStartTime()),
        subscription.getFixedRate().getNano()
    );
    subscriptionDao.save(subscription);
  }

  public static Instant toNext(Instant time, Duration d) {
    Instant next = time;
    while (Instant.now().isAfter(next)) {
      next = next.plus(d);
    }
    return next;
  }

}
