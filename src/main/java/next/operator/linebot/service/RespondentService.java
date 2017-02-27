package next.operator.linebot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.util.Arrays;
import java.util.List;

/**
 * 處理純文字類型的訪問
 */
@Service
public class RespondentService {

  @Autowired
  private List<RespondentExecutable> executors;

  /** 處理所有/開頭進來的, 被視為命令 */
  public String commend(String message) {
    final String[] commend = message.trim().split(" ");
    final String method = commend[0];
    final String[] args = Arrays.copyOfRange(commend, 1, commend.length);
    return executors.stream()
        .filter(e -> e.structure().split(" ")[0].equals(method))
        .findAny()
        .map(e -> e.execute(args))
        .orElseThrow(() -> new ValidationException("我不認識【" + message + "】這個指令喔，你要不要找別人來試試看？"));
  }

}
