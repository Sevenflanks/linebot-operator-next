package next.operator.linebot.service;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 指令介面
 */
public interface RespondentExecutable {

  /** 語法結構(允許多種) */
  String[] structures();

  /** 執行 */
  String execute(MessageEvent<TextMessageContent> event, String... args);

  default String structure() {
    return Stream.of(structures()).collect(Collectors.joining("\n"));
  }

}
