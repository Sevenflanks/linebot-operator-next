package next.operator.subscription.dao;

import next.operator.common.persistence.GenericDao;
import next.operator.subscription.entity.Subscription;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionDao extends GenericDao<Subscription> {

  List<Subscription> findBySubscriber_SubscriberId(String subscriberId);

}
