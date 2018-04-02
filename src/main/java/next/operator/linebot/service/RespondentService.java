package next.operator.linebot.service;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import lombok.extern.slf4j.Slf4j;
import next.operator.user.dao.UserDao;
import next.operator.will.client.WillClient;
import next.operator.will.exception.WillException;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import javax.validation.ValidationException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * 處理純文字類型的訪問
 */
@Slf4j
@Service
public class RespondentService {

  @Autowired
  private List<RespondentExecutable> executors;

  @Autowired
  private List<RespondentTalkable> talkers;

  @Autowired
  private LineMessagingClient client;

  @Autowired
  private WillClient willClient;

  @Autowired
  private UserDao userDao;

  public static ThreadLocal<List<Term>> currentTern = new ThreadLocal<>();
  public static ThreadLocal<MessageEvent<TextMessageContent>> currentEvent = new ThreadLocal<>();

  public static boolean isCommand(String message) {
    return message.trim().startsWith("/") || message.trim().startsWith("！");
  }

  public TextMessage response(MessageEvent<TextMessageContent> event) {
    final String message = event.getMessage().getText();

    // 一些常用的物件處理
    currentTern.set(NlpAnalysis.parse(message).getTerms());
    currentEvent.set(event);

    final TextMessage response;
    if (isCommand(message)) {
      response = new TextMessage(commend(event, message));
    } else if (WillClient.pattern.matcher(message).find()) {
      response = toWill(event);
    } else {
      response = nativeLanguage(event).map(TextMessage::new).orElse(null);
    }

    currentTern.remove();
    currentEvent.remove();

    return response;
  }

  /** 給薇兒的指令 */
  private TextMessage toWill(MessageEvent<TextMessageContent> event) {
    String response;
    try {
      client.pushMessage(new PushMessage(event.getSource().getSenderId(), new TextMessage("收到了要給薇兒的訊息！稍等～我幫你找她哦..."))).get();
      response = willClient.talkToWill(event);
      if (response == null) {
        response = "薇兒不理你耶...";
      } else {
        response = "薇兒說：\n" + response;
      }
    } catch (WillException e) {
      response = "打開的方式好像不對喔！薇兒說：\n" + e.getMessage();
    } catch (ResourceAccessException | HttpClientErrorException e) {
      response = "薇兒好像還在睡耶...找不到人QQ";
    } catch (HttpServerErrorException e) {
      response = "奇怪，薇兒的電話好像壞了，快找人來修理呀！" + "(" + e.getRawStatusCode() + ")";
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      response = "糟糕，我的電話好像壞了，快找人來修理呀！\n" + e.getClass().getSimpleName() + ":" + e.getMessage();
    }
    client.pushMessage(new PushMessage(event.getSource().getSenderId(), new TextMessage(response)));

    return null;
  }

  /** 處理所有/開頭進來的, 被視為命令 */
  private String commend(MessageEvent<TextMessageContent> event, String message) {
    final String[] commend = message.trim().split(" ");
    final String method = commend[0];
    final String[] args = Arrays.copyOfRange(commend, 1, commend.length);
    return executors.stream()
        .filter(e -> Stream.of(e.structures()).anyMatch(s -> s.split(" ")[0].equals(method)))
        .findAny()
        .map(e -> e.execute(event, args))
        .orElseThrow(() -> new ValidationException(userDao.getCurrentUserName() + "我不認識【" + message + "】這個指令喔，你要不要找別人來試試看？"));
  }

  /** 處理所有原生語言命令 */
  private Optional<String> nativeLanguage(MessageEvent<TextMessageContent> event) {
    // 進行文句分詞
    return talkers.stream()
            .filter(r -> ifThen(r.isReadable(event.getMessage().getText()), event, r.doFirst(client)))
            .findAny()
            .map(r -> r.talk(event.getMessage().getText()));
  }

  /** 若為True則做某件事, 否則沒事 */
  private boolean ifThen(boolean bool, MessageEvent<TextMessageContent> event, Consumer<MessageEvent<TextMessageContent>> consumer) {
    if (bool && consumer != null) {
      consumer.accept(event);
    }
    return bool;
  }

}
