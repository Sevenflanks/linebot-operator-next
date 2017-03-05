package next.operator.user.dao;

import next.operator.common.AbstractDao;
import org.springframework.stereotype.Component;

@Component
public class UserDao extends AbstractDao<String> {

  public ThreadLocal<String> currentUserName = new ThreadLocal<>();

  public String getCurrentUserName() {
    return currentUserName.get() == null ? "" : currentUserName.get();
  }
}
