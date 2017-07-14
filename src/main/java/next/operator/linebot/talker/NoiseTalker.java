package next.operator.linebot.talker;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import next.operator.ChineseTokens;
import next.operator.linebot.service.RespondentReadable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 回垃圾話用的
 * 當對話中出現[要不要][去不去]等N不N文法時
 * 隨機回垃圾話: N or 不N
 */
@Service
public class NoiseTalker implements RespondentReadable {

  @Autowired
  private ChineseTokens chineseTokens;

  final Pattern readPattern = Pattern.compile("([\\u4e00-\\u9fa5A-Za-z]+?)不(\\1[\\u4e00-\\u9fa5A-Za-z]*)");
  final String[] response = {"", "不"};
  final String[] suffixes = {"", "喔", "唄", "啦", "唷"};

  @Override
  public boolean isReadable(String message) {
    return readPattern.matcher(message).find();
  }

  @Override
  public Consumer<MessageEvent<TextMessageContent>> doFirst(LineMessagingClient client) {
    return null;
  }

  @Override
  public String talk(String message) {
    final Matcher matcher = readPattern.matcher(message);
    if (matcher.find()) {
      final String keyWord = chineseTokens.run(matcher.group(2))[0];
      return response[(int) (Math.random() * response.length)] + keyWord + suffixes[(int) (Math.random() * suffixes.length)];
    } else {
      return null;
    }
  }
}
