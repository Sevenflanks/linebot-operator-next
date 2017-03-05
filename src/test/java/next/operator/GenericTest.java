package next.operator;

import next.operator.currency.service.CurrencyService;
import next.operator.searchoil.service.OilService;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class GenericTest {

  @Before
  public void setUp() throws Exception {

    // 等待所有查詢資料回來
    while(CurrencyService.exrateDatas == null || OilService.oilPriceDatas == null) {
      Thread.sleep(1000);
    }

  }

}