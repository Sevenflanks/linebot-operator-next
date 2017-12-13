package next.operator.linebot.talker;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import next.operator.currency.enums.CurrencyType;
import next.operator.currency.model.CurrencyExrateModel;
import next.operator.currency.service.CurrencyService;
import next.operator.linebot.executor.impl.ExrateExecutor;
import next.operator.linebot.service.RespondentTalkable;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.StreamSupport;

/**
 * 當對話中出現錢幣關鍵字時提供匯率資料
 */
@Service
public class ExrateTalker implements RespondentTalkable {

  @Autowired
  private ExrateExecutor exrateExecutor;

  @Autowired
  private CurrencyService currencyService;

  private ThreadLocal<CurrencyType> currentMached = new ThreadLocal<>();

  @Override
  public boolean isReadable(String message) {
    final Iterator<Term> sourceIterator = NlpAnalysis.parse(message).iterator();
    Iterable<Term> iterable = () -> sourceIterator;
    final Optional<CurrencyType> matchedCurrenctType = StreamSupport.stream(iterable.spliterator(), false)
        .map(Term::getName)
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
    final CurrencyExrateModel exrate = currencyService.getExrate(matchedCurrenctType.name(), CurrencyType.TWD.name());
    return "我感覺到了你想知道" + matchedCurrenctType.getFirstLocalName() + "的匯率！\n" +
        "1 " + exrate.getExFrom() + " = " + exrateExecutor.fullDecimalFormat.format(exrate.getExrate()) + " " + exrate.getExTo() +
        ", 資料時間：" + exrateExecutor.dateTimeFormatter.format(exrate.getTime());
  }

}
