package next.operator.scheduled;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import next.operator.currency.service.CurrencyExtRateDataLoader;
import next.operator.currency.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定期查全球匯率
 * 本段程式使用了全球即時匯率API(https://tw.rter.info/howto_currencyapi.php)
 */
@Slf4j
@Component
@Order(1)
public class CurrencyJob {

  @Autowired
  private CurrencyExtRateDataLoader currencyExtRateDataLoader;

  @Scheduled(fixedRate = 10 * 60 * 1000)
  @PostConstruct
  public void load() {
    log.debug("loading CurrencyExtRateData");
    try {
      CurrencyService.exrateDatas = currencyExtRateDataLoader.load();
      log.info("loading CurrencyExtRateData success");
    } catch (Exception e) {
      log.error("loading CurrencyExtRateData failed", e);
    }
  }

}
