package next.operator.user.dao;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.event.source.GroupSource;
import com.linecorp.bot.model.event.source.RoomSource;
import com.linecorp.bot.model.event.source.Source;
import com.linecorp.bot.model.profile.UserProfileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class UserDao {

  @Autowired
  private LineMessagingClient client;

  public ThreadLocal<String> currentUserName = new ThreadLocal<>();
  public ThreadLocal<Source> currentSource = new ThreadLocal<>();

  public String getCurrentUserName() {

    if (currentUserName.get() != null) {
      return currentUserName.get();
    } else {
      final Source source = currentSource.get();
      final CompletableFuture<UserProfileResponse> userProfileResponse;
      if (source instanceof RoomSource) {
        userProfileResponse = client.getRoomMemberProfile(((RoomSource) source).getRoomId(), source.getUserId());
      } else if (source instanceof GroupSource) {
        userProfileResponse = client.getGroupMemberProfile(((GroupSource) source).getGroupId(), source.getUserId());
      } else {
        userProfileResponse = client.getProfile(source.getUserId());
      }

      String displayName;
      try {
        displayName = userProfileResponse.get().getDisplayName();
      } catch (Exception ignore) {
        displayName = "";
      }
      currentUserName.set(displayName);
      return displayName;
    }

  }

  public void clear() {
    currentSource.remove();
    currentUserName.remove();
  }
}
