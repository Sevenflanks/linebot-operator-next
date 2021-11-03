package next.operator.utils;

import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NumberUtils {

  public static Double tryDouble(String source) {
    try {
      return Double.parseDouble(source);
    } catch (NumberFormatException e) {
      return null;
    }
  }

  public static BigDecimal calc(String evaluation) throws EvaluationException {
    final Evaluator evaluator = new Evaluator();
    return new BigDecimal(evaluator.evaluate(evaluation));
  }

  private static final Map<Character, Integer> CHINESE_MAP_NUM = Stream.of("0零〇,1壹一,2貳二兩,3參三,4肆四,5伍五,6陸六,7柒七,8捌八,9玖九".split(","))
      .flatMap(c -> c.substring(1).chars().mapToObj(s -> "" + c.charAt(0) + ((char)s)))
      .collect(Collectors.toMap(c -> c.charAt(1), c -> c.charAt(0) - '0'));

  public static final Double zhNumConvertToInt(String source) {

    try {
      if (source == null) {
        return 0D;
      }

      if (source.contains("載")){
        final String[] parts = source.split("載");
        return carry(parts, 1e44);
      }

      if (source.contains("正")){
        final String[] parts = source.split("正");
        return carry(parts, 1e40);
      }

      if (source.contains("澗")){
        final String[] parts = source.split("澗");
        return carry(parts, 1e36);
      }

      if (source.contains("溝")){
        final String[] parts = source.split("溝");
        return carry(parts, 1e32);
      }

      if (source.contains("穰")){
        final String[] parts = source.split("穰");
        return carry(parts, 1e28);
      }

      if (source.contains("秭")){
        final String[] parts = source.split("秭");
        return carry(parts, 1e24);
      }

      if (source.contains("垓")){
        final String[] parts = source.split("垓");
        return carry(parts, 1e20);
      }

      if (source.contains("京")){
        final String[] parts = source.split("京");
        return carry(parts, 1e16);
      }

      if (source.contains("兆")){
        final String[] parts = source.split("兆");
        return carry(parts, 1e12);
      }

      if (source.contains("億")){
        final String[] parts = source.split("億");
        return carry(parts, 1e8);
      }

      if (source.contains("萬")){
        final String[] parts = source.split("萬");
        return carry(parts, 1e4);
      }

      if (source.startsWith("十")){
        source = "一" + source;
      }

      final String toCalc = source.chars()
          .mapToObj(c -> {
            final int idx = CHINESE_MAP_NUM.getOrDefault((char) c, -1);
            if (idx >= 0) {
              return "+" + idx;
            } else {
              return "*" + ('拾' == c || '十' == c ? 1e1 : '佰' == c || '百' == c ? 1e2 : '仟' == c || '千' == c? 1e3 : 0);
            }
          })
          .collect(Collectors.joining());

      return calc(toCalc).doubleValue();
    } catch (Exception e) {
      return null;
    }
  }

  private static double carry(String[] parts, double v) throws EvaluationException {
    if (parts.length > 1) {
      return zhNumConvertToInt(parts[0]) * v + zhNumConvertToInt(parts[1]);
    } else {
      return zhNumConvertToInt(parts[0]) * v;
    }
  }

  public static void main(String[] args) {
    System.out.println(zhNumConvertToInt("三十"));
    System.out.println(zhNumConvertToInt("30"));
  }

}
