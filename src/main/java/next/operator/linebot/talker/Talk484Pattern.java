package next.operator.linebot.talker;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class Talk484Pattern implements AbstractNoiscePattern {

  final static Pattern READ_PATTERN = Pattern.compile("484");
  final String[] response = {"4", "84", "是", "不是"};

  @Override
  public NoisceMatcher match(String input) {
    return new NoisceMatcher(READ_PATTERN.matcher(input), matcher -> response[(int) (Math.random() * response.length)] + NoiseTalker.SUFFIXES[(int) (Math.random() * NoiseTalker.SUFFIXES.length)]);
  }
}
