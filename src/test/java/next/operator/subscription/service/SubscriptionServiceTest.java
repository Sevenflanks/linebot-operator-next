package next.operator.subscription.service;

import next.operator.GenericTest;
import next.operator.subscription.entity.Subscriber;
import next.operator.subscription.entity.Subscription;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;

public class SubscriptionServiceTest extends GenericTest {

  @Autowired
  private SubscriptionService subscriptionService;

  @Test
  public void testValidator() {
    final Subscription subscription = new Subscription();
    final Subscriber subscriber = new Subscriber();
    subscription.setSubscriber(subscriber);
    subscription.setFixedRate(Duration.ofSeconds(100000));
    subscriptionService.subscribe(subscription);
  }

}