package next.operator.will.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResponseModel {

  private String data;
  private List<String> messages;

  public boolean isMessageEmpty() {
    return messages == null || messages.isEmpty();
  }

}
