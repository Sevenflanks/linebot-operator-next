package next.operator.will.client;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import next.operator.will.exception.WillException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/** 串薇兒的API */
@Component
public class WillClient {

  @Value("${will.url}")
  private String willUrl;

  private RestTemplate restTemplate = new RestTemplate();

  /** 丟整個event給她 */
  public String talkToWill(MessageEvent<TextMessageContent> event) {
    final ResponseEntity<String> response = restTemplate.postForEntity(willUrl, event, String.class);
    if (response.getStatusCodeValue() == 200) {
      return response.getBody();
    } else {
      // 若發生錯誤則拿訊息回來
      throw new WillException(response.getStatusCode().getReasonPhrase());
    }
  }

}
