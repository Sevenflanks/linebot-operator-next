package next.operator.rolldice.service;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;
import next.operator.rolldice.model.DiceModel;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * 擲骰功能
 */
@Slf4j
@Service
public class DiceService {

  /**
   * @param times 骰子數量
   * @param dice  骰面最大值(1~N)
   * @return 每顆骰的結果
   */
  public Integer[] roll(int times, int dice) {
    return roll(IntStream.range(0, times).mapToObj(i -> dice));
  }

  /**
   * @param dices 骰面最大值(1~N)
   * @return 每顆骰的結果
   */
  public Integer[] roll(Integer[] dices) {
    return roll(Stream.of(dices));
  }

  /**
   * @param dices 骰面最大值(1~N)
   * @return 每顆骰的結果
   */
  private Integer[] roll(Stream<Integer> dices) {
    return dices.map(i -> ThreadLocalRandom.current().nextInt(1, i + 1)).toArray(Integer[]::new);
  }

  public Integer[] roll(DiceModel dice) {
    final Evaluator evaluator = new Evaluator();
    return IntStream
        .range(0, dice.getTimes())
        .map(i -> ThreadLocalRandom.current().nextInt(1, dice.getMax() + 1))
        .mapToObj(r -> evalCorrection(r, dice, evaluator))
        .toArray(Integer[]::new);
  }

  private Integer evalCorrection(int rollResult, DiceModel dice, Evaluator evaluator) {
    if (!Strings.isNullOrEmpty(dice.getCorrection())) {
      try {
        final Integer newResult = new BigDecimal(evaluator.evaluate(rollResult + dice.getCorrection())).intValue();
        rollResult = newResult;
      } catch (EvaluationException e) {
        throw new ValidationException("你的補正算式【" + dice.getCorrection() + "】怪怪的喔");
      }
    }
    return rollResult;
  }

}
