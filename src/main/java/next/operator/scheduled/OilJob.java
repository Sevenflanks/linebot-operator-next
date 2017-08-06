package next.operator.scheduled;

import lombok.extern.slf4j.Slf4j;
import next.operator.searchoil.model.OliPriceModel;
import next.operator.searchoil.service.OilPriceLoader;
import next.operator.searchoil.service.OilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 定期查油價
 */
@Slf4j
@Component
@Order(2)
public class OilJob {

  @Autowired
  private OilPriceLoader oilPriceLoader;

  @Scheduled(fixedRate = 60 * 60 * 1000)
  @PostConstruct
  public void load() {
    log.debug("loading OilPriceData");
    try {
      final List<OliPriceModel> datas = oilPriceLoader.load();
      OilService.oilPriceDatas = datas;
      log.info("loading OilPriceData success");
    } catch (Exception e) {
      log.error("loading OilPriceData failed", e);
    }
  }

}
