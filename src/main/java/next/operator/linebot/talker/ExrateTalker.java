package next.operator.linebot.talker;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import next.operator.ChineseTokens;
import next.operator.currency.enums.CurrencyType;
import next.operator.currency.model.CurrencyExrateModel;
import next.operator.currency.service.CurrencyService;
import next.operator.linebot.executor.impl.ExrateExecutor;
import next.operator.linebot.service.RespondentTalkable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * 當對話中出現錢幣關鍵字時提供匯率資料
 */
@Service
public class ExrateTalker implements RespondentTalkable {

  @Autowired
  private ChineseTokens chineseTokens;

  @Autowired
  private ExrateExecutor exrateExecutor;

  @Autowired
  private CurrencyService currencyService;

  private ThreadLocal<CurrencyType> currentMached = new ThreadLocal<>();

  @Override
  public boolean isReadable(String message) {
    final String[] words = chineseTokens.run(message);
    final Optional<CurrencyType> matchedCurrenctType = Stream.of(words)
        .map(CurrencyType::tryParseByName)
        .filter(Optional::isPresent)
        .filter(o -> CurrencyType.TWD != o.get()) // 台幣不算
        .map(Optional::get)
        .findFirst();

    matchedCurrenctType.ifPresent(currentMached::set);
    return matchedCurrenctType.isPresent();
  }

  @Override
  public Consumer<MessageEvent<TextMessageContent>> doFirst(LineMessagingClient client) {
    return null;
  }

  @Override
  public String talk(String message) {
    final CurrencyType matchedCurrenctType = currentMached.get();
    currentMached.remove();
    final CurrencyExrateModel exrate = currencyService.getExrate(CurrencyType.TWD.name(), matchedCurrenctType.name());
    return "我感覺到了你想知道匯率！" +
        "1 " + CurrencyType.TWD.name() + " = " + exrateExecutor.fullDecimalFormat.format(exrate.getExrate()) + " " + exrate.getExTo() +
        ", 資料時間：" + exrateExecutor.dateTimeFormatter.format(exrate.getTime());
  }
}
