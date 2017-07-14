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
import next.operator.will.model.ResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import javax.validation.ValidationException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
  private List<RespondentReadable> readers;

  @Autowired
  private LineMessagingClient client;

  @Autowired
  private WillClient willClient;

  @Autowired
  private UserDao userDao;

  public TextMessage response(MessageEvent<TextMessageContent> event) {
    final String message = event.getMessage().getText();
    if (message.trim().startsWith("/") || message.trim().startsWith("！")) {
      return new TextMessage(commend(message));
    } else if (message.trim().startsWith("薇兒")) {
      return toWill(event);
    } else {
      return nativeLanguage(message).map(TextMessage::new).orElse(null);
    }
  }

  private TextMessage toWill(MessageEvent<TextMessageContent> event) {
    client.pushMessage(new PushMessage(event.getSource().getSenderId(), new TextMessage("收到了要給薇兒的訊息！稍等～我幫你找她哦...")));
    String response;
    try {
      final ResponseModel model = willClient.talkToWill(event);
      if (!model.isMessageEmpty()) {
        response = model.getMessages().stream().collect(Collectors.joining("\n"));
      } else if (model.getData() == null) {
        response = "薇兒不理你耶...";
      } else {
        response = "薇兒說：\n" + model.getData();
      }
    } catch (WillException e) {
      response = "打開的方式好像不對喔！薇兒說：\n" + e.getMessage();
    } catch (HttpClientErrorException e) {
      response = "薇兒好像還在睡耶...找不到人QQ" + "(" + e.getRawStatusCode() + ")";
    } catch (HttpServerErrorException e) {
      response = "奇怪，薇兒的電話好像壞了，快找人來修理呀！" + "(" + e.getRawStatusCode() + ")";
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      response = "糟糕，我的電話好像壞了，快找人來修理呀！";
    }
    client.pushMessage(new PushMessage(event.getSource().getSenderId(), new TextMessage(response)));

    return null;
  }

  /** 處理所有/開頭進來的, 被視為命令 */
  String commend(String message) {
    final String[] commend = message.trim().split(" ");
    final String method = commend[0];
    final String[] args = Arrays.copyOfRange(commend, 1, commend.length);
    return executors.stream()
        .filter(e -> Stream.of(e.structures()).anyMatch(s -> s.split(" ")[0].equals(method)))
        .findAny()
        .map(e -> e.execute(args))
        .orElseThrow(() -> new ValidationException(userDao.getCurrentUserName() + "我不認識【" + message + "】這個指令喔，你要不要找別人來試試看？"));
  }

  /** 處理所有原生語言命令 */
  Optional<String> nativeLanguage(String message) {
    return readers.stream()
            .filter(r -> r.isReadable(message))
            .findAny()
            .map(r -> r.talk(message));
  }

}
