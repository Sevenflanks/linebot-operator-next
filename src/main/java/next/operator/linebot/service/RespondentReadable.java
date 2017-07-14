package next.operator.linebot.service;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;

import java.util.function.Consumer;

/**
 * 原生語言介面
 */
public interface RespondentReadable {

  /** 看懂否 */
  boolean isReadable(String message);

  /** 先做啥 */
  Consumer<MessageEvent<TextMessageContent>> doFirst(LineMessagingClient client);

  /** 執行 */
  String talk(String message);
}
