package next.operator.linebot.service;

import next.operator.user.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * 處理純文字類型的訪問
 */
@Service
public class RespondentService {

  @Autowired
  private List<RespondentExecutable> executors;

  @Autowired
  private UserDao userDao;

  /** 處理所有/開頭進來的, 被視為命令 */
  public String commend(String message) {
    final String[] commend = message.trim().split(" ");
    final String method = commend[0];
    final String[] args = Arrays.copyOfRange(commend, 1, commend.length);
    return executors.stream()
        .filter(e -> Stream.of(e.structures()).anyMatch(s -> s.split(" ")[0].equals(method)))
        .findAny()
        .map(e -> e.execute(args))
        .orElseThrow(() -> new ValidationException(userDao.getCurrentUserName() + "我不認識【" + message + "】這個指令喔，你要不要找別人來試試看？"));
  }

}
