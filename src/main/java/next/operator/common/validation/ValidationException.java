package next.operator.common.validation;

import lombok.Getter;

import java.util.List;

public class ValidationException extends IllegalArgumentException {

  @Getter
  private List<String> messages;

  public ValidationException(String message) {
    super(message);
    this.messages = List.of(message);
  }

  public ValidationException(List<String> messages) {
    super(String.join("; ", messages));
    this.messages = messages;
  }

}
