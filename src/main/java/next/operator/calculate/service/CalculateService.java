package next.operator.calculate.service;

import lombok.extern.slf4j.Slf4j;
import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.math.BigDecimal;

/**
 * 計算功能
 */
@Slf4j
@Service
public class CalculateService {

  public BigDecimal calc(String evaluation) {
    try {
      final Evaluator evaluator = new Evaluator();
      return new BigDecimal(evaluator.evaluate(evaluation));
    } catch (EvaluationException e) {
      throw new ValidationException("你的算式【" + evaluation + "】字太醜了，我看不懂耶QQ");
    }
  }

}
