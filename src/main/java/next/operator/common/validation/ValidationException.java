package next.operator.common.validation;

import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

public class ValidationException extends javax.validation.ValidationException {

  @Getter
  private List<String> messages;

  ValidationException(List<String> messages) {
    this.messages = messages;
  }

  @Override
  public String getMessage() {
    return this.messages.stream().collect(Collectors.joining(","));
  }

}
