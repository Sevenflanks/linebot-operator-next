package next.operator.currency.service;

import lombok.extern.slf4j.Slf4j;
import next.operator.AppTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {AppTest.class})
public class CurrencyServiceTest {

  @Autowired
  private CurrencyService currencyService;

  @Before
  public void setUp() throws Exception {

    // 等待匯率資料進來
    while(CurrencyService.exrateDatas == null) {
      Thread.sleep(1000);
    }

  }

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