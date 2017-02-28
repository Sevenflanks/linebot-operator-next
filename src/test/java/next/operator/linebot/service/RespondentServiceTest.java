package next.operator.linebot.service;

import lombok.extern.slf4j.Slf4j;
import next.operator.GenericTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class RespondentServiceTest extends GenericTest {

  @Autowired
  private RespondentService respondentService;

  @Test
  public void commend() throws Exception {
    print("/help");
    print("/exrate twd jpy");
    print("/exrate 100 twd jpy");
    print("/exrate 10.5 usd jpy");
  }

  private void print(String commend) {
    System.out.println(respondentService.commend(commend));
  }

}