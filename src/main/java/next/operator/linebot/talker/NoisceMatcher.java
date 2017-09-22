package next.operator.linebot.talker;

import java.util.function.Function;
import java.util.regex.Matcher;

public class NoisceMatcher {

  private boolean matched;

  private Matcher matcher;

  private Function<Matcher, String> responser;

  public NoisceMatcher(Matcher matcher, Function<Matcher, String> responser) {
    this.matched = matcher.find();
    this.matcher = matcher;
    this.responser = responser;
  }

  public boolean isMatched() {
    return matched;
  }

  public String response() {
    return responser.apply(matcher);
  }

}
