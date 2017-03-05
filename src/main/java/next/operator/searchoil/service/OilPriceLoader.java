package next.operator.searchoil.service;

import com.google.common.collect.Lists;
import next.operator.searchoil.model.OliPriceModel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * 取得當前及時油價
 * 本程式使用到了 油價,汽油價格查詢預測(http://www.taiwanoil.org/)
 */
@Service
public class OilPriceLoader {

  private final String dataUr = "http://www.taiwanoil.org/z.php?z=oiltw&c=94abf0&tz=Asia/Taipei&tf=1";

  public List<OliPriceModel> load() throws IOException {
    final Document doc = Jsoup.connect(dataUr).get();
    final Elements tr = doc.select("tr");
    Element element = tr.first();

    List<OliPriceModel> result = Lists.newArrayList();
    while(element.nextElementSibling() != null) {
      element = element.nextElementSibling();
      final String provider = element.select("td").eq(0).text();
      if (provider.contains("中油") || provider.contains("台塑")) {
        final BigDecimal price = new BigDecimal(element.select("td").eq(1).text());
        result.add(new OliPriceModel(provider, price));
      }
    }

    return result;
  }

}
