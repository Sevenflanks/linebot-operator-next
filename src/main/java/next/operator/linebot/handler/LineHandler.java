package next.operator.linebot.handler;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.JoinEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import lombok.extern.slf4j.Slf4j;
import next.operator.common.validation.ValidationException;
import next.operator.linebot.service.RespondentService;
import next.operator.user.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Profile("prod")
@Slf4j
@Component
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
    setCurrentUser(event);

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
      client.replyMessage(new ReplyMessage(event.getReplyToken(), response));
      // push有使用次數限制
      //      client.pushMessage(new PushMessage(event.getSource().getSenderId(), response));
    }
    return null;
  }

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
    client.replyMessage(new ReplyMessage(event.getReplyToken(), new TextMessage("安安你好" + message + "掰掰去洗澡~")));
  }

  /**
   * 被加入群組
   */
  @EventMapping
  public void handleJoinEvent(JoinEvent event) {
    log.info("Joined: {}", event);
    client.replyMessage(new ReplyMessage(event.getReplyToken(), new TextMessage("安安你好掰掰去洗澡~")));
  }

  private void setCurrentUser(Event event) {
    userDao.currentSource.set(event.getSource());
  }

  private void cleanCurrentUser() {
    userDao.clear();
  }

}
