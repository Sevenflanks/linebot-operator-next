package next.operator.linebot.talker;

import next.operator.GenericTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ExrateTalkerTest extends GenericTest {

  @Autowired
  private ExrateTalker exrateTalker;

  @Test
  public void test() {
    System.out.println(exrateTalker.isReadable("我來看看我可以花4500日幣買到多少日幣的書"));
    System.out.println(exrateTalker.isReadable("4500日幣可以"));
  }

}