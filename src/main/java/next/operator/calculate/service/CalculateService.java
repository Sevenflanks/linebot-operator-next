package next.operator.calculate.service;

import lombok.extern.slf4j.Slf4j;
import net.sourceforge.jeval.EvaluationException;
import next.operator.utils.NumberUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.math.BigDecimal;

/**
 * 計算功能
 */
@Slf4j
@Component
public class CalculateService {

  public BigDecimal calc(String evaluation) {
    try {
      return NumberUtils.calc(evaluation);
    } catch (EvaluationException e) {
      throw new ValidationException("你的算式【" + evaluation + "】字太醜了，我看不懂耶QQ");
    }
  }

}
