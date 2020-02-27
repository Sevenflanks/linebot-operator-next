package next.operator.currency.service;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import next.operator.config.WebObjectMapper;
import next.operator.currency.model.CurrencyExrateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 取得當前的全球即時匯率
 * 本程式使用到了 全球即時匯率API(https://tw.rter.info/howto_currencyapi.php)
 */
@Slf4j
@Component
public class CurrencyExtRateDataLoader {

  private final URI dataUrl;

  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  private WebObjectMapper objectMapper;

  public CurrencyExtRateDataLoader() throws URISyntaxException {
    this.dataUrl = new URI("https://tw.rter.info/capi.php");
  }

  public List<CurrencyExrateModel> load() throws IOException {
    final ResponseEntity<String> responseEntity = restTemplate.getForEntity(dataUrl, String.class);

    final HashMap<String, HashMap<String, String>> raw =
        objectMapper.readValue(
            responseEntity.getBody(),
            new TypeReference<HashMap<String, HashMap<String, String>>>() {}
        );

    return parse(raw);
  }

  public static final String EX_FROM = "USD"; // 國際貨幣組織目前對於貨幣轉換均採用美金報價
  private static final String RAW_FIELD_TIME = "UTC";
  private static final String RAW_FIELD_EXTRATE = "Exrate";
  private static final DateTimeFormatter UTC_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
  private List<CurrencyExrateModel> parse(HashMap<String, HashMap<String, String>> raw) {
    return raw.entrySet().stream()
        .map(e -> {
          final String key = e.getKey();
          final String extTo = key.substring(EX_FROM.length());
          final LocalDateTime utc = LocalDateTime.parse(e.getValue().get(RAW_FIELD_TIME), UTC_FORMATTER);
          final LocalDateTime localDateTime = OffsetDateTime.of(utc, ZoneOffset.UTC).withOffsetSameInstant(ZoneOffset.ofHours(8)).toLocalDateTime();
          final BigDecimal extrate = BigDecimal.valueOf(Double.valueOf(e.getValue().get(RAW_FIELD_EXTRATE)));

          final CurrencyExrateModel model = new CurrencyExrateModel();
          model.setExFrom(EX_FROM);
          model.setExTo(extTo);
          model.setTime(localDateTime);
          model.setExrate(extrate);

          return model;
        })
        .collect(Collectors.toList());
  }

}
