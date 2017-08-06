package next.operator.common.uncheck;

import java.util.function.Function;

public abstract class Uncheck {

  public static <T, R> Function<T, R> apply(UncheckFunction<T, R> function) {
    return function;
  }

}
