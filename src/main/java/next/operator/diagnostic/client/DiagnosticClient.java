package next.operator.diagnostic.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import next.operator.diagnostic.exception.DiagnosticException;
import next.operator.diagnostic.model.DiagnosticModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** 診斷網址狀況 */
@Component
public class DiagnosticClient {

  private static final String DIAGNOSTIC_URL = "https://www.google.com/safebrowsing/diagnostic";
  private static final int TIME_OUT = 5_000;
  private static final Pattern RESP_PATTERN = Pattern.compile("\\{.*\\}"); // 取得回應的json的方式

  @Autowired
  private ObjectMapper objectMapper;

  private final RestTemplate restTemplate;

  public DiagnosticClient() {
    restTemplate = new RestTemplate();
    final SimpleClientHttpRequestFactory rf = (SimpleClientHttpRequestFactory)restTemplate.getRequestFactory();
    rf.setReadTimeout(TIME_OUT);
    rf.setConnectTimeout(TIME_OUT);
  }

  public DiagnosticModel diagnostic(String url) throws IOException {
    final Map<String, String> args = Maps.newHashMap();
    args.put("output", "pb");
    args.put("site", url);
    final ResponseEntity<String> response = restTemplate.getForEntity(DIAGNOSTIC_URL, String.class, args);

    if (response.getStatusCodeValue() == 200) {
      final Matcher matcher = RESP_PATTERN.matcher(response.getBody());
      if (matcher.find()) {
        return objectMapper.readValue(matcher.group(), DiagnosticModel.class);
      } else {
        throw new DiagnosticException("大神API給的格式好像改了喔QQ");
      }
    } else {
      throw new DiagnosticException("檢查網址的工具壞了喔！(" + response.getStatusCode().value() + " " + response.getStatusCode().getReasonPhrase() + ")");
    }
  }

}
