package next.operator.linebot.executor.impl;

import next.operator.linebot.executor.FunctionExecutable;
import next.operator.linebot.executor.HelperExecutable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HelpExecuter implements HelperExecutable {

  @Autowired
  private List<FunctionExecutable> executors;

  @Override
  public String structure() {
    return "/help";
  }

  @Override
  public String execute(String... args) {
    return "目前所有可以用的語法如下：\n" +
    executors.stream()
        .map(FunctionExecutable::structure)
        .collect(Collectors.joining("\n"));
  }

}
