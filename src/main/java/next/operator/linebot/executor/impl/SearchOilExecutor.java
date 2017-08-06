package next.operator.linebot.executor.impl;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import next.operator.linebot.executor.FunctionExecutable;
import next.operator.searchoil.service.OilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.stream.Collectors;

@Service
public class SearchOilExecutor implements FunctionExecutable {

  @Autowired
  private OilService oilService;

  @Override
  public String[] structures() {
    return new String[]{
        "/oil",
        "！油"
    };
  }


  final DecimalFormat decimalFormat = new DecimalFormat("#0.00");
  @Override
  public String execute(MessageEvent<TextMessageContent> event, String... args) {
    return "今日每公升油價資訊如下：\n" +
        oilService.get().stream().map(m -> m.getProvider() + "：" + decimalFormat.format(m.getPrice()) + "元").collect(Collectors.joining("\n"));
  }

}
