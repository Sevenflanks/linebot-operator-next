package next.operator.linebot.executor.impl;

import next.operator.currency.model.CurrencyExrateModel;
import next.operator.currency.service.CurrencyService;
import next.operator.linebot.executor.FunctionExecutable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;

@Service
public class ExrateExecuter implements FunctionExecutable {

  @Autowired
  private CurrencyService currencyService;

  @Override
  public String structure() {
    return "/exrate 幣別1 幣別2";
  }

  final DecimalFormat decimalFormat = new DecimalFormat("#,###,###,##0.00000");
  final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
  @Override
  public String execute(String... args) {
    if (args.length != 2) {
      return "打開的方式不對喔！要像這樣子: " + structure();
    } else {
      final CurrencyExrateModel exrate = currencyService.getExrate(args[0], args[1]);
      return "匯率查詢：" +
          "1 " + exrate.getExFrom() + " = " + decimalFormat.format(exrate.getExrate()) + " " + exrate.getExTo() +
          ", 資料時間：" + dateTimeFormatter.format(exrate.getTime());
    }
  }

}
