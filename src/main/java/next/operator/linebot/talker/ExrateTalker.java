package next.operator.linebot.talker;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import next.operator.currency.enums.CurrencyType;
import next.operator.currency.model.CurrencyExrateModel;
import next.operator.currency.service.CurrencyService;
import next.operator.linebot.executor.impl.ExrateExecutor;
import next.operator.linebot.service.RespondentService;
import next.operator.linebot.service.RespondentTalkable;
import next.operator.utils.NumberUtils;
import org.ansj.domain.Term;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * 當對話中出現錢幣關鍵字時提供匯率資料
 */
@Service
public class ExrateTalker implements RespondentTalkable {

  @Autowired
  private ExrateExecutor exrateExecutor;

  @Autowired
  private CurrencyService currencyService;

  private ThreadLocal<Term> matched = new ThreadLocal<>();
  private ThreadLocal<Double> amount = new ThreadLocal<>();

  @Override
  public boolean isReadable(String message) {
    // 檢查是否有存在符合幣別的單字
    final List<Term> terms = RespondentService.currentTern.get();
    final Optional<Term> matchedTerm = terms.stream()
        .filter(t -> {
          final Optional<CurrencyType> currencyType = CurrencyType.tryParseByName(t.getName());
          return currencyType.filter(type -> CurrencyType.TWD != type).isPresent();
        })
        .findFirst();

    final boolean isMatch = matchedTerm.isPresent();

    if (isMatch) {
      // 檢查是否存在數字
      final double sum = terms.stream()
          .filter(t -> "m".equals(t.getNatureStr()) || "nw".equals(t.getNatureStr()))
          .mapToDouble(t -> Optional.ofNullable(NumberUtils.tryDouble(t.getName())).orElseGet(() -> Optional.ofNullable(NumberUtils.zhNumConvertToInt(t.getName())).orElse(1D)))
          .sum();

      amount.set(sum);
      matched.set(matchedTerm.get());
    } else {
      matched.remove();
      amount.remove();
    }

    return isMatch;
  }

  @Override
  public Consumer<MessageEvent<TextMessageContent>> doFirst(LineMessagingClient client) {
    return null;
  }

  @Override
  public String talk(String message) {
    final Term term = matched.get();
    final Double amount = Optional.ofNullable(this.amount.get()).filter(n -> n != 0).orElse(1D);
    matched.remove();
    this.amount.remove();

    final CurrencyType matchedCurrenctType = CurrencyType.tryParseByName(term.getName()).get(); // isReadable已經檢查過，必定有值

    final CurrencyExrateModel exrate = currencyService.getExrate(matchedCurrenctType.name(), CurrencyType.TWD.name());
    return "我感覺到了你想知道" + matchedCurrenctType.getFirstLocalName() + "的匯率！\n" +
        exrateExecutor.decimalFormat.format(BigDecimal.valueOf(amount)) + " " + exrate.getExFrom() + " = " + exrateExecutor.decimalFormat.format(exrate.getExrate().multiply(BigDecimal.valueOf(amount))) + " " + exrate.getExTo() +
        ", 資料時間：" + exrateExecutor.dateTimeFormatter.format(exrate.getTime());
  }

}
