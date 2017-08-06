package next.operator.config;

import next.operator.ChineseTokens;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;

import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.util.Collections;

@Configuration
@ComponentScan(basePackages = {
    "next.operator.**.dao",
    "next.operator.**.service",
    "next.operator.linebot.executor",
})
public class ServiceConfig {

  @Bean
  @Primary
  public WebObjectMapper webObjectMapper() {
    return new WebObjectMapper();
  }

  @Bean
  public RestTemplate restTemplate() {
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.setInterceptors(Collections.singletonList(new UserAgentInterceptor()));
    return restTemplate;
  }

  class UserAgentInterceptor implements ClientHttpRequestInterceptor {
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
      HttpHeaders headers = request.getHeaders();
      headers.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
      return execution.execute(request, body);
    }
  }

  @Bean
  public Validator validator() {
    return Validation.buildDefaultValidatorFactory().getValidator();
  }

  @Bean
  public ChineseTokens chineseTokens() throws IOException, ClassNotFoundException {
    return new ChineseTokens(
            this.getClass().getResourceAsStream("/tw_training.zip"),
            "Big5",
            5,
            5.0,
            5000,
            256,
            0.0,
            0.0);
  }

}
