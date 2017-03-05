package next.operator.linebot.handler;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.Multicast;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.*;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
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

    final String msgTxt = event.getMessage().getText();
    TextMessage response = null;

    try {
      if (msgTxt.trim().startsWith("/") || msgTxt.trim().startsWith("！")) {
        response = new TextMessage(respondentService.commend(msgTxt));
      }
    } catch (ValidationException e) {
      response = new TextMessage(e.getMessage());
    } catch (Exception e) {
      log.info(e.getMessage(), e);
    }

    cleanCurrentUser();
    return response;
  }

  @EventMapping
  public void handleDefaultMessageEvent(Event event) {
    log.info("DefaultMessageEvent: {}", event);
  }

  /**
   * 封鎖/刪除好友
   */
  @EventMapping
  public void handleUnfollowEvent(UnfollowEvent event) {
    log.info("Unfollowed this bot: {}", event);
  }

  /**
   * 加入好友
   */
  @EventMapping
  public void handleFollowEvent(FollowEvent event) {
    log.info("Followed this bot: {}", event);
    String message;
    try {
      userDao.insert(event.getSource().getUserId());
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

  @EventMapping
  public void handlePostbackEvent(PostbackEvent event) {
    log.info("Postbacked: {}", event);
  }

  @EventMapping
  public void handleBeaconEvent(BeaconEvent event) {
    log.info("Beacon: {}", event);
  }

  @EventMapping
  public void handleOtherEvent(Event event) {
    log.debug("Received message(Ignored): {}", event);
  }

  public void sendToAll(String message) {
    client.multicast(new Multicast(userDao.findAll(), new TextMessage(message)));
  }

  private void setCurrentUserName(Event event) {
    try {
      userDao.currentUserName.set(client.getProfile(event.getSource().getUserId()).get().getDisplayName());
    } catch (InterruptedException | ExecutionException ignored) {
    }
  }

  private void cleanCurrentUser() {
    userDao.currentUserName.set(null);
  }

}
