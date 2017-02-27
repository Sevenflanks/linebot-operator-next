package next.operator.currency.service;

import next.operator.currency.model.CurrencyExrateModel;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.util.List;

@Service
public class CurrencyService {

  public static List<CurrencyExrateModel> exrateDatas;

  public CurrencyExrateModel getExrate(String exFrom, String exTo) {
    exFrom = exFrom.toUpperCase();
    exTo = exTo.toUpperCase();

    final CurrencyExrateModel exFromTo;
    if (exFrom.equals(exTo)) {
      throw new ValidationException("自己轉自己匯率是零，這匯率很棒，快錢給我我幫你轉。");
    } else if (CurrencyExtRateDataLoader.EX_FROM.equals(exFrom)) {
      exFromTo = getUSDExTo(exTo);
    } else {
      exFromTo = getFromExUSD(exFrom).merge(getUSDExTo(exTo));
    }

    return exFromTo;
  }

  private CurrencyExrateModel getFromExUSD(String exFrom) {
    return getExrate(exFrom).reverse();
  }

  private CurrencyExrateModel getUSDExTo(String exTo) {
    return getExrate(exTo);
  }

  private CurrencyExrateModel getExrate(String currency) {
    return exrateDatas.stream()
        .filter(d -> d.getExTo().equals(currency))
        .findAny()
        .orElseThrow(() -> new ValidationException("殘念，我好像不認識【" + currency + "】，你要不要試試別家？"));
  }
}
