package next.operator.linebot.handler;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.Multicast;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.event.*;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import lombok.extern.slf4j.Slf4j;
import next.operator.user.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ExecutionException;

@Slf4j
@LineMessageHandler
public class LineHandler {

    @Autowired
    private LineMessagingClient client;

    @Autowired
    private UserDao userDao;

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        log.info("TextMessageEvent: {}", event);
        return new TextMessage(event.getMessage().getText());
    }

    @EventMapping
    public void handleDefaultMessageEvent(Event event) {
        log.info("DefaultMessageEvent: {}", event);
    }

    /** 封鎖/刪除好友 */
    @EventMapping
    public void handleUnfollowEvent(UnfollowEvent event) {
        log.info("Unfollowed this bot: {}", event);
    }

    /** 加入好友 */
    @EventMapping
    public void handleFollowEvent(FollowEvent event) {
        log.info("Followed this bot: {}", event);
        String message;
        try {
            userDao.insert(event.getSource().getUserId());
            message = "Hello " + client.getProfile(event.getSource().getUserId()).get().getDisplayName() +
                    ", I've seen you now~";
        } catch (InterruptedException | ExecutionException e) {
            message = "Hello World! Dear Unknow. Congratulations! I can't recognize you.";
            log.error("get profile failed. userId:" + event.getSource().getUserId(), e);
        }
        client.pushMessage(new PushMessage(event.getSource().getUserId(), new TextMessage(message)));
    }

    /** 被加入群組 */
    @EventMapping
    public void handleJoinEvent(JoinEvent event) {
        log.info("Joined: {}", event);
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
        log.info("Received message(Ignored): {}", event);
    }

    public void sendToAll(String message) {
        client.multicast(new Multicast(userDao.findAll(), new TextMessage(message)));
    }

}
