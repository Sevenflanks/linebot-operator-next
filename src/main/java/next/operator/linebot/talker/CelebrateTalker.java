package next.operator.linebot.talker;

import com.google.common.collect.Lists;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import next.operator.linebot.service.RespondentService;
import next.operator.linebot.service.RespondentTalkable;
import next.operator.record.entity.Record;
import org.ansj.domain.Term;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * 新年快樂，中秋節快樂等，總之遇到快樂的時候把前面的字拿出來複誦一遍
 */
@Component
public class CelebrateTalker implements RespondentTalkable {

  private static final String KEY_WORD = "快樂";
  private ThreadLocal<String> celebration = new ThreadLocal<>();

  // 因為慶祝會很多人同時進行，避免短時間內發太多次，要暫時記住已經發過的部分
  private static volatile List<Record> celebrated = Lists.newArrayList();

  @Override
  public boolean isReadable(String message) {
    final List<Term> terms = RespondentService.currentTern.get();
    final Optional<Term> key = terms.stream().filter(t -> KEY_WORD.equals(t.getName())).findFirst();
    final int idx = key.map(terms::indexOf).orElse(-1);
    if (idx > 0 && idx < terms.size()) {
      final String currentCelebration = terms.get(idx - 1).getName();
      final String senderId = RespondentService.currentEvent.get().getSource().getSenderId();
      final LocalDateTime now = LocalDateTime.now();

      // 尋找最近一個相符的紀錄
      final Optional<Record> record = celebrated.stream()
          .filter(c -> senderId.equals(c.getTalkedTo()) && currentCelebration.equals(c.getKey()))
          .findFirst();

      // 判斷是否剛說過
      final boolean isJustTalked = record
          .filter(c -> Duration.between(c.getTalkedTime(), now).getSeconds() < 600)
          .isPresent();

      if (isJustTalked) {
        return false;
      } else {
        // 若過去紀錄曾存在則洗掉
        record.ifPresent(celebrated::remove);
        // 紀錄發送時間與發送對象
        celebrated.add(new Record(currentCelebration, senderId, now));
        celebration.set(currentCelebration);
        return true;
      }
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
