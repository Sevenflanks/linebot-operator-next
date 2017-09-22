package next.operator.linebot.talker;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class HaveOrNotNoiscePattern implements NoiscePattern {

  final static Pattern READ_PATTERN = Pattern.compile("有沒有");
  final String[] response = {"有", "沒有"};

  @Override
  public NoisceMatcher match(String input) {
    return new NoisceMatcher(READ_PATTERN.matcher(input), matcher -> response[(int) (Math.random() * response.length)] + NoiseTalker.SUFFIXES[(int) (Math.random() * NoiseTalker.SUFFIXES.length)]);
  }
}
