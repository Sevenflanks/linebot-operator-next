package next.operator.subscription.dao;

import next.operator.common.persistence.GenericDao;
import next.operator.subscription.entity.Subscription;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionDao extends GenericDao<Subscription> {

  Subscription findBySubscriber_SubscriberId(String subscriberId);

}
