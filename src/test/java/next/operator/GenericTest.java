package next.operator;

import next.operator.currency.service.CurrencyService;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class GenericTest {

  @Before
  public void setUp() throws Exception {

    // 等待匯率資料進來
    while(CurrencyService.exrateDatas == null) {
      Thread.sleep(1000);
    }

  }

}