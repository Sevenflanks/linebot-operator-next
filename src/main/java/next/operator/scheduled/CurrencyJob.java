package next.operator.scheduled;

import lombok.extern.slf4j.Slf4j;
import next.operator.currency.model.CurrencyExrateModel;
import next.operator.currency.service.CurrencyExtRateDataLoader;
import next.operator.currency.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 定期查全球匯率
 * 本段程式使用了全球即時匯率API(https://tw.rter.info/howto_currencyapi.php)
 */
@Slf4j
@Component
public class CurrencyJob {

  @Autowired
  private CurrencyExtRateDataLoader currencyExtRateDataLoader;

  @Scheduled(fixedRate = 10 * 60 * 1000)
  public void load() {
    log.debug("loading CurrencyExtRateData");
    try {
      final List<CurrencyExrateModel> datas = currencyExtRateDataLoader.load();
      CurrencyService.exrateDatas = datas;
      log.info("loading CurrencyExtRateData success");
    } catch (Exception e) {
      log.error("loading CurrencyExtRateData failed", e);
    }
  }

}
