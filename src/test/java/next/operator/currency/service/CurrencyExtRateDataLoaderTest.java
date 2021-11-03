package next.operator.currency.service;

import next.operator.GenericTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class CurrencyExtRateDataLoaderTest extends GenericTest {

  @Autowired
  private CurrencyExtRateDataLoader currencyExtRateDataLoader;

  @Test
  public void load() throws Exception {
    currencyExtRateDataLoader.load().forEach(System.out::println);
  }

}