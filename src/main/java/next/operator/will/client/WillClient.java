package next.operator.will.client;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import next.operator.will.exception.WillException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.Collections;

/** 串薇兒的API */
@Component
public class WillClient {

  private static final int TIME_OUT = 5_000;

  @Value("${will.url}")
  private String willUrl;

  private final RestTemplate restTemplate;
  private final HttpHeaders httpHeaders;

  public WillClient() {
    httpHeaders = new HttpHeaders();
    httpHeaders.setAccept(Collections.singletonList(MediaType.TEXT_PLAIN)); // 目前約定返回純文字就好

    restTemplate = new RestTemplate();
    restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
    final SimpleClientHttpRequestFactory rf = (SimpleClientHttpRequestFactory)restTemplate.getRequestFactory();
    rf.setReadTimeout(TIME_OUT);
    rf.setConnectTimeout(TIME_OUT);
  }

  /** 丟整個event給她 */
  public String talkToWill(MessageEvent<TextMessageContent> event) {
    final ResponseEntity<String> response =
        restTemplate.exchange(willUrl, HttpMethod.POST, new HttpEntity<>(httpHeaders), String.class, event);
    if (response.getStatusCodeValue() == 200) {
      return response.getBody();
    } else {
      // 若發生錯誤則拿訊息回來
      throw new WillException(response.getStatusCode().getReasonPhrase());
    }
  }

}
