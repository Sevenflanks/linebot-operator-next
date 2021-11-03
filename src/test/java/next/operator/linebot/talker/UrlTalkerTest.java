package next.operator.linebot.talker;

import next.operator.GenericTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UrlTalkerTest extends GenericTest {

  @Autowired UrlTalker urlTalker;

  @Test
  public void talk() throws Exception {

    final String talk = urlTalker.talk("http://img.2cat.org/~tedc21thc/live/src/1500035493383.jpg");
    System.out.println(talk);

  }

}