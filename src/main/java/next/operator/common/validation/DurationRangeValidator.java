package next.operator.common.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.Duration;

/** 檢查Duration的範圍(秒) */
public class DurationRangeValidator implements ConstraintValidator<DurationRange, Duration> {

  private int minSeconds;
  private int maxSeconds;

  @Override
  public void initialize(DurationRange constraintAnnotation) {
    minSeconds = constraintAnnotation.minSeconds();
    maxSeconds = constraintAnnotation.maxSeconds();
  }

  @Override
  public boolean isValid(Duration value, ConstraintValidatorContext context) {
    if (value != null && (value.getSeconds() < minSeconds || value.getSeconds() > maxSeconds)) {
      return false;
    } else {
      return true;
    }
  }

}
