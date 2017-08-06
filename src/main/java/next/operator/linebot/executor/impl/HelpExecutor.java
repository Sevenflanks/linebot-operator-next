package next.operator.linebot.executor.impl;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import next.operator.linebot.executor.FunctionExecutable;
import next.operator.linebot.executor.HelperExecutable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class HelpExecutor implements HelperExecutable {

  @Autowired
  private List<FunctionExecutable> executors;

  @Override
  public String[] structures() {
    return new String[]{
        "/help",
        "/?",
        "！？"
    };
  }

  @Override
  public String execute(MessageEvent<TextMessageContent> event, String... args) {
    return "目前所有可以用的語法如下：\n" +
    executors.stream()
        .map(FunctionExecutable::structures)
        .flatMap(Stream::of)
        .collect(Collectors.joining("\n"));
  }

}
