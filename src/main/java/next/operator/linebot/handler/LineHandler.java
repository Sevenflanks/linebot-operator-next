package next.operator.linebot.handler;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.JoinEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.GroupSource;
import com.linecorp.bot.model.event.source.RoomSource;
import com.linecorp.bot.model.event.source.Source;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import lombok.extern.slf4j.Slf4j;
import next.operator.linebot.service.RespondentService;
import next.operator.user.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ValidationException;
import java.util.concurrent.ExecutionException;

@Slf4j
@LineMessageHandler
public class LineHandler {

  @Autowired
  private LineMessagingClient client;

  @Autowired
  private UserDao userDao;

  @Autowired
  private RespondentService respondentService;

  @EventMapping
  public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
    log.debug("TextMessageEvent: {}", event);
    setCurrentUserName(event);

    TextMessage response = null;

    try {
      response = respondentService.response(event);
    } catch (ValidationException e) {
      response = new TextMessage(e.getMessage());
    } catch (Exception e) {
      log.info(e.getMessage(), e);
    }

    cleanCurrentUser();
    if (response != null) {
      // ReplyToken的可靠度太低了, 很容易因為逾時或覆蓋而過期
      client.pushMessage(new PushMessage(event.getSource().getSenderId(), response));
    }
    return null;
  }

//  @EventMapping
//  public void handleDefaultMessageEvent(Event event) {
//    log.info("DefaultMessageEvent: {}", event);
//  }

//  /**
//   * 封鎖/刪除好友
//   */
//  @EventMapping
//  public void handleUnfollowEvent(UnfollowEvent event) {
//    log.info("Unfollowed this bot: {}", event);
//  }

  /**
   * 加入好友
   */
  @EventMapping
  public void handleFollowEvent(FollowEvent event) {
    log.info("Followed this bot: {}", event);
    String message;
    try {
      message = client.getProfile(event.getSource().getUserId()).get().getDisplayName();
    } catch (InterruptedException | ExecutionException e) {
      message = "";
      log.error("get profile failed. userId:" + event.getSource().getUserId(), e);
    }
    client.pushMessage(new PushMessage(event.getSource().getUserId(), new TextMessage("安安你好" + message + "掰掰去洗澡~")));
  }

  /**
   * 被加入群組
   */
  @EventMapping
  public void handleJoinEvent(JoinEvent event) {
    log.info("Joined: {}", event);
    client.replyMessage(new ReplyMessage(event.getReplyToken(), new TextMessage("安安你好掰掰去洗澡~")));
  }

//  @EventMapping
//  public void handlePostbackEvent(PostbackEvent event) {
//    log.info("Postbacked: {}", event);
//  }

//  @EventMapping
//  public void handleBeaconEvent(BeaconEvent event) {
//    log.info("Beacon: {}", event);
//  }

//  @EventMapping
//  public void handleOtherEvent(Event event) {
//    log.debug("Received message(Ignored): {}", event);
//  }

  private void setCurrentUserName(Event event) {
    try {
      final Source source = event.getSource();
      final UserProfileResponse userProfileResponse;
      if (source instanceof RoomSource) {
        userProfileResponse = client.getRoomMemberProfile(((RoomSource) source).getRoomId(), source.getUserId()).get();
      } else if (source instanceof GroupSource) {
        userProfileResponse = client.getGroupMemberProfile(((GroupSource) source).getGroupId(), source.getUserId()).get();
      } else {
        userProfileResponse = client.getProfile(source.getUserId()).get();
      }
      userDao.currentUserName.set(userProfileResponse.getDisplayName());
    } catch (InterruptedException | ExecutionException ignored) {
    }
  }

  private void cleanCurrentUser() {
    userDao.currentUserName.set(null);
  }

}
