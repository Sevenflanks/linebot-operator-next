package next.operator.common.uncheck;

import java.util.function.Function;

@FunctionalInterface
public interface UncheckFunction<T, R> extends Function<T, R> {

  R applyUncheck(T t);

  @Override
  default R apply(T t) {
    try {
      return applyUncheck(t);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
