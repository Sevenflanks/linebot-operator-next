package next.operator.common.uncheck;

import java.util.function.Function;
import java.util.function.Supplier;

public abstract class Uncheck {

  public static <T, R> Function<T, R> apply(UncheckFunction<T, R> function) {
    return function;
  }

  public static <T> Supplier<T> get(UncheckSupplier<T> supplier) {
    return supplier;
  }

}
