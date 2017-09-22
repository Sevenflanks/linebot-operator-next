package next.operator.linebot.talker;

import next.operator.ChineseTokens;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class DefaultNoiscePattern implements NoiscePattern {

  @Autowired
  private ChineseTokens chineseTokens;

  final static Pattern READ_PATTERN = Pattern.compile("([\\u4e00-\\u9fa5A-Za-z]+?)不(\\1[\\u4e00-\\u9fa5A-Za-z]*)");
  final String[] response = {"", "不"};

  @Override
  public NoisceMatcher match(String input) {
    return new NoisceMatcher(READ_PATTERN.matcher(input), matcher -> {
      final String keyWord = chineseTokens.run(matcher.group(2))[0];
      return response[(int) (Math.random() * response.length)] + keyWord + NoiseTalker.SUFFIXES[(int) (Math.random() * NoiseTalker.SUFFIXES.length)];
    });
  }

}
