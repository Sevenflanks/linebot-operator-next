package next.operator.linebot.service;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import org.ansj.domain.Term;

import java.util.List;
import java.util.function.Consumer;

/**
 * 原生語言介面
 */
public interface RespondentTalkable {

  /** 看懂否 */
  boolean isReadable(String message, List<Term> terms);

  /** 先做啥 */
  Consumer<MessageEvent<TextMessageContent>> doFirst(LineMessagingClient client);

  /** 執行 */
  String talk(String message);
}
