package next.operator.linebot.talker;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import lombok.extern.slf4j.Slf4j;
import next.operator.diagnostic.client.DiagnosticClient;
import next.operator.diagnostic.exception.DiagnosticException;
import next.operator.diagnostic.model.DiagnosticModel;
import next.operator.linebot.service.RespondentReadable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 若對話中出現網址, 則嘗試進行解析
 */
@Slf4j
@Service
public class UrlTalker implements RespondentReadable {

  @Autowired
  private DiagnosticClient diagnosticClient;

  final Pattern readPattern = Pattern.compile("(http[s]?|ftp)://[^\\s]*+");

  @Override
  public boolean isReadable(String message) {
    return readPattern.matcher(message).find();
  }

  @Override
  public Consumer<MessageEvent<TextMessageContent>> doFirst(LineMessagingClient client) {
    // 一接收到可以判斷的網址時先發訊息通知
    return event -> {
      try {
        client.pushMessage(new PushMessage(event.getSource().getSenderId(), new TextMessage("發現了網址，來幫你檢查一下"))).get();
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    };
  }

  @Override
  public String talk(String message) {
    try {
      final Matcher matcher = readPattern.matcher(message);
      if (matcher.find()) {
        final String url = matcher.group();
        final DiagnosticModel response = diagnosticClient.diagnostic(url);
        return response.toString();
      } else {
        return null;
      }
    } catch (DiagnosticException e) {
      return e.getMessage();
    } catch (ResourceAccessException | HttpClientErrorException | HttpServerErrorException e) {
      log.error(e.getMessage(), e);
      return "跟估狗大神的連線好像怪怪的，檢查不了，你等一下再來試試";
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return "檢查網址安全性的功能壞掉嚕，快叫工程師來加班！\n" + e.getClass().getSimpleName() + ":" + e.getMessage();
    }
  }

}
