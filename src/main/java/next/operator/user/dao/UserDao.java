package next.operator.user.dao;

import com.linecorp.bot.model.profile.UserProfileResponse;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class UserDao {

  public ThreadLocal<String> currentUserName = new ThreadLocal<>();
  public ThreadLocal<CompletableFuture<UserProfileResponse>> currentUserNameFuture = new ThreadLocal<>();

  public String getCurrentUserName() {
    try {
      if (currentUserName.get() != null) {
        return currentUserName.get();
      } else {
        final String displayName = currentUserNameFuture.get().get().getDisplayName();
        currentUserName.set(displayName);
        return displayName;
      }
    } catch (Exception ignored) {
      return "";
    }
  }

  public void clear() {
    currentUserNameFuture.remove();
    currentUserName.remove();
  }
}
