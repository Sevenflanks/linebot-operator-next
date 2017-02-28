package next.operator.currency.service;

import lombok.extern.slf4j.Slf4j;
import next.operator.GenericTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class CurrencyServiceTest extends GenericTest {

  @Autowired
  private CurrencyService currencyService;

  @Test
  public void getExrate() throws Exception {

    log.info("result={}", currencyService.getExrate("USD", "TWD"));
    log.info("result={}", currencyService.getExrate("USD", "JPY"));
    log.info("result={}", currencyService.getExrate("TWD", "JPY"));
    log.info("result={}", currencyService.getExrate("JPY", "TWD"));
    log.info("result={}", currencyService.getExrate("TWD", "USD"));
    log.info("result={}", currencyService.getExrate("JPY", "USD"));

  }

}