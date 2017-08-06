package next.operator.common.validation;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintViolation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public abstract class ValidationUtils {

  public static void requiredMsgsEmpty(Stream<String>... msgs) {
    final List<String> allMsgs = Stream.of(msgs)
        .flatMap(Function.identity())
        .filter(s -> !Strings.isNullOrEmpty(s))
        .collect(Collectors.toList());
    if (!allMsgs.isEmpty()) {
      throw new ValidationException(allMsgs);
    }
  }

  public static void requiredMsgsEmpty(List<String>... msgs) {
    final List<String> allMsgs = Stream.of(msgs)
        .flatMap(List::stream)
        .filter(s -> !Strings.isNullOrEmpty(s))
        .collect(Collectors.toList());
    if (!allMsgs.isEmpty()) {
      throw new ValidationException(allMsgs);
    }
  }

  public static <T> List<String> from(Set<ConstraintViolation<T>> violations) {
    List<String> msgs = Lists.newArrayList();
    for (ConstraintViolation<?> violation : violations) {
      msgs.add(from(violation));
    }
    return msgs;
  }

  public static <T> String from(ConstraintViolation<T> violation) {
    return Optional.ofNullable(violation.getRootBeanClass())
        .map(c -> getField(violation, c))
        .map(f -> f.getAnnotation(Label.class))
        .map(l -> "【" + l.value() + "】")
        .orElseGet(() -> violation.getPropertyPath().toString()) +
        violation.getMessage();
  }

  private static Field getField(ConstraintViolation<?> v, Class<?> c) {
    final String fieldName = v.getPropertyPath().toString();
    try {
      return c.getDeclaredField(fieldName);
    } catch (NoSuchFieldException e) {
      log.error("parses label failed: " + fieldName, e);
      return null;
    }
  }

}
