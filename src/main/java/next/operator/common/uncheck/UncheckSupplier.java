package next.operator.common.uncheck;

import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

@FunctionalInterface
public interface UncheckSupplier<T> extends Supplier<T> {

  T getUncheck() throws ExecutionException, InterruptedException;

  @Override
  default T get() {
    try {
      return getUncheck();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
