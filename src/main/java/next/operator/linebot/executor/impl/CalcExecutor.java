package next.operator.linebot.executor.impl;

import next.operator.calculate.service.CalculateService;
import next.operator.linebot.executor.FunctionExecutable;
import next.operator.user.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CalcExecutor implements FunctionExecutable {

  @Autowired
  private CalculateService calculateService;

  @Autowired
  private UserDao userDao;

  @Override
  public String[] structures() {
    return new String[]{
        "/calc 算式 [算式...]",
        "！算 算式 [算式...]"
    };
  }

  @Override
  public String execute(String... args) {

    StringBuilder sb = new StringBuilder();
    sb.append("算好了！").append(userDao.getCurrentUserName()).append("剛剛給的算式答案是這樣：\n");
    if (args.length > 0) {
      for (String evaluation : args) {
        sb.append(evaluation).append(" = ").append(calculateService.calc(evaluation)).append("\n");
      }
    } else {
      sb.append("...欸？你沒有寫算式哦！答案是零");
    }
    return sb.toString();
  }

}
