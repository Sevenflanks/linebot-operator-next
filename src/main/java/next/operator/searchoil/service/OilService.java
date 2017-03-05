package next.operator.searchoil.service;

import next.operator.searchoil.model.OliPriceModel;
import org.springframework.stereotype.Service;

import java.util.List;

/** 油價查詢 */
@Service
public class OilService {

  public static List<OliPriceModel> oilPriceDatas;

  public List<OliPriceModel> get() {
    return oilPriceDatas;
  }

}
