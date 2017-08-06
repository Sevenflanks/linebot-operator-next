package next.operator.common.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = {DurationRangeValidator.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
public @interface DurationRange {

  String message() default "{next.operator.common.validation.DurationRange}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  int minSeconds() default 0;

  int maxSeconds() default 60 * 60 * 24;

}
