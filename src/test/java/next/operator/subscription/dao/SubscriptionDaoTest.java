package next.operator.subscription.dao;

import next.operator.GenericTest;
import next.operator.subscription.entity.Subscriber;
import next.operator.subscription.entity.Subscription;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.time.Duration;
import java.time.Instant;

public class SubscriptionDaoTest extends GenericTest {

  @Autowired
  private SubscriptionDao subscriptionDao;

  @Test
  @Transactional
  public void test() {

    final Subscription subscription = new Subscription();
    final Subscriber subscriber = new Subscriber();

    subscriber.setSubscriberId("TEST-ID");
    subscriber.setSubscriberName("TEST-NAME");
    subscriber.setSubscribeTo("TEST-TO");

    subscription.setSubscriber(subscriber);
    subscription.setStartTime(Instant.now());
    subscription.setFixedRate(Duration.ofSeconds(1));
    subscription.setLastPushTime(Instant.now().minusSeconds(100));
    subscription.setMsg("TEST-MSG");

    final Subscription saved = subscriptionDao.save(subscription);
    final Long savedId = saved.getId();

    Assertions.assertThat(saved.getId()).isNotNull();
    Assertions.assertThat(saved.getCreateTime()).isNotNull();
    Assertions.assertThat(saved.getModifyTime()).isNull();

    Assertions.assertThat(saved.getSubscriber()).isNotNull();
    Assertions.assertThat(saved.getSubscriber().getSubscriberId()).isNotNull();
    Assertions.assertThat(saved.getSubscriber().getSubscriberName()).isNotNull();
    Assertions.assertThat(saved.getSubscriber().getSubscribeTo()).isNotNull();

    Assertions.assertThat(saved.getStartTime()).isNotNull();
    Assertions.assertThat(saved.getFixedRate()).isNotNull();
    Assertions.assertThat(saved.getLastPushTime()).isNotNull();
    Assertions.assertThat(saved.getMsg()).isNotNull();
    Assertions.assertThat(saved.isNew()).isFalse();

    final String newMsg = "TEST-MSG-2";
    saved.setMsg(newMsg);
    final Subscription updated = subscriptionDao.saveAndFlush(saved);
    Assertions.assertThat(updated.getId()).isEqualTo(savedId);
    Assertions.assertThat(updated.getModifyTime()).isNotNull();
    Assertions.assertThat(updated.getMsg()).isEqualTo(newMsg);

    final Subscription found = subscriptionDao.findById(savedId)
        .orElseThrow(() -> new ValidationException("ID:" + savedId + ")這一則訂閱不存在喔！"));
    Assertions.assertThat(found).isNotNull();

    subscriptionDao.deleteById(savedId);
    subscriptionDao.flush();
    final Subscription foundAfterDelete = subscriptionDao.findById(savedId)
        .orElseThrow(() -> new ValidationException("ID:" + savedId + ")這一則訂閱不存在喔！"));
    Assertions.assertThat(foundAfterDelete).isNull();

  }

}
