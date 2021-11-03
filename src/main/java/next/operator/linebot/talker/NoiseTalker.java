package next.operator.linebot.talker;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import next.operator.linebot.service.RespondentTalkable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * 回垃圾話用的
 * 當對話中出現[要不要][去不去]等N不N文法時
 * 隨機回垃圾話: N or 不N
 */
@Service
public class NoiseTalker implements RespondentTalkable {
  public static final String[] SUFFIXES = {"", "喔", "唄", "啦", "唷"};

  @Autowired
  private List<AbstractNoiscePattern> extraPatterns;

  private ThreadLocal<NoisceMatcher> currentMached = new ThreadLocal<>();

  @Override
  public boolean isReadable(String message) {
    if (!message.contains("詩")) {
      // 如果沒被叫到名字, 就不要回應了, 被嫌吵太多次XD
      return false;
    }
    final Optional<NoisceMatcher> matched = extraPatterns.stream()
        .map(np -> np.match(message))
        .filter(NoisceMatcher::isMatched)
        .findAny();
    matched.ifPresent(currentMached::set);
    return matched.isPresent();
  }

  @Override
  public Consumer<MessageEvent<TextMessageContent>> doFirst(LineMessagingClient client) {
    return null;
  }

  @Override
  public String talk(String message) {
    final NoisceMatcher matcher = currentMached.get();
    currentMached.remove();
    return Optional.ofNullable(matcher).map(NoisceMatcher::response).orElse(null);
  }
}
