package next.operator.linebot.executor.impl;

import com.google.common.primitives.Doubles;
import next.operator.currency.model.CurrencyExrateModel;
import next.operator.currency.service.CurrencyService;
import next.operator.linebot.executor.FunctionExecutable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class ExrateExecuter implements FunctionExecutable {

  @Autowired
  private CurrencyService currencyService;

  @Override
  public String structure() {
    return "/exrate [幣別1數量] 幣別1 幣別2";
  }

  final DecimalFormat decimalFormat = new DecimalFormat("#,###,###,##0.0#####");
  final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
  @Override
  public String execute(String... args) {
    if (args.length == 2) {
      final CurrencyExrateModel exrate = currencyService.getExrate(args[0], args[1]);
      return "匯率查詢：" +
          "1 " + exrate.getExFrom() + " = " + decimalFormat.format(exrate.getExrate()) + " " + exrate.getExTo() +
          ", 資料時間：" + dateTimeFormatter.format(exrate.getTime());
    } else if (args.length == 3) {
      final Double amount = Optional.ofNullable(Doubles.tryParse(args[0]))
          .orElseThrow(() -> new ValidationException("我覺得你的第一個參數不是數字欸，你要不要問一下數學老師？"));
      final CurrencyExrateModel exrate = currencyService.getExrate(args[1], args[2]);
      return "匯率查詢：" +
          decimalFormat.format(amount) + " " + exrate.getExFrom() + " = " + decimalFormat.format(exrate.getExrate().multiply(BigDecimal.valueOf(amount))) + " " + exrate.getExTo() +
          ", 資料時間：" + dateTimeFormatter.format(exrate.getTime());
    } else {
      return "打開的方式不對喔！要像這樣子: " + structure();
    }
  }

}
