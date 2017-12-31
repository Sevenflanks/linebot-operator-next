package next.operator.linebot.talker;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import next.operator.config.BasicInfo;
import next.operator.linebot.service.RespondentTalkable;
import org.ansj.domain.Term;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;

/**
 * 祝我生日快樂
 */
public class BirthdayTalker implements RespondentTalkable {

  @Override
  public boolean isReadable(String message, List<Term> terms) {
    // 必須是生日當天
    if (!BasicInfo.BIRTHDAY.equals(LocalDate.now())) {
      return false;
    }

    //


    return false;
  }

  @Override
  public Consumer<MessageEvent<TextMessageContent>> doFirst(LineMessagingClient client) {
    return null;
  }

  @Override
  public String talk(String message) {
    return null;
  }

}
