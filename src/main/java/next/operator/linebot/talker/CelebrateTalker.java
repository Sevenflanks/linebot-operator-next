package next.operator.linebot.talker;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import next.operator.linebot.service.RespondentTalkable;
import org.ansj.domain.Term;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * 新年快樂，中秋節快樂等，總之遇到快樂的時候把前面的字拿出來複誦一遍
 */
@Service
public class CelebrateTalker implements RespondentTalkable {

  private static final String KEY_WORD = "快樂";
  private ThreadLocal<String> celebration = new ThreadLocal<>();

  @Override
  public boolean isReadable(String message, List<Term> terms) {
    final Optional<Term> key = terms.stream().filter(t -> KEY_WORD.equals(t.getName())).findFirst();
    final int idx = key.map(terms::indexOf).orElse(-1);
    if (idx > 0 && idx < terms.size()) {
      celebration.set(terms.get(idx - 1).getName());
      return true;
    } else {
      return false;
    }
  }

  @Override
  public Consumer<MessageEvent<TextMessageContent>> doFirst(LineMessagingClient client) {
    return null;
  }

  @Override
  public String talk(String message) {
    return celebration.get() + KEY_WORD + NoiseTalker.SUFFIXES[(int) (Math.random() * NoiseTalker.SUFFIXES.length)];
  }

}
