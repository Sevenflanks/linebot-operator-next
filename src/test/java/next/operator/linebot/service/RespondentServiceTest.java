package next.operator.linebot.service;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
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
    print("！匯率 10.5 usd jpy");

    print("/roll 1d6");
    print("/roll 6d6");
    print("/roll 6d6 2d10");
    print("！骰 6d6 2d10");
    print("/roll 6d6+1");
    print("/roll 6d6*5");
    print("/roll 10d3*2-2");

    print("/oil");

    print("W:1101");
  }

  private void print(String commend) {
    final MessageEvent<TextMessageContent> event =
        new MessageEvent<TextMessageContent>(null, null, new TextMessageContent(null, commend), null);
    System.out.println(respondentService.response(event).getText());
  }

}