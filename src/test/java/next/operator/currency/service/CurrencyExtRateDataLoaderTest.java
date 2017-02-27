package next.operator.currency.service;

import next.operator.AppTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {AppTest.class})
public class CurrencyExtRateDataLoaderTest {

  @Autowired
  private CurrencyExtRateDataLoader currencyExtRateDataLoader;

  @Test
  public void load() throws Exception {
    currencyExtRateDataLoader.load().forEach(System.out::println);
}

}