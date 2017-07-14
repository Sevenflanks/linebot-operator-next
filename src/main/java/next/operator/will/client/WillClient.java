package next.operator.will.client;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import next.operator.will.exception.WillException;
import next.operator.will.model.ResponseModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

/** 串薇兒的API */
@Component
public class WillClient {

  private static final int TIME_OUT = 5_000;

  @Value("${will.url}")
  private String willUrl;

  private final RestTemplate restTemplate;

  public WillClient() {
    restTemplate = new RestTemplate();
    restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
    final SimpleClientHttpRequestFactory rf = (SimpleClientHttpRequestFactory)restTemplate.getRequestFactory();
    rf.setReadTimeout(TIME_OUT);
    rf.setConnectTimeout(TIME_OUT);
  }

  /** 丟整個event給她 */
  public ResponseModel talkToWill(MessageEvent<TextMessageContent> event) {
    final ResponseEntity<ResponseModel> response = restTemplate.postForEntity(willUrl, event, ResponseModel.class);
    if (response.getStatusCodeValue() == 200) {
      return response.getBody();
    } else {
      // 若發生錯誤則拿訊息回來
      throw new WillException(response.getStatusCode().getReasonPhrase());
    }
  }

}
