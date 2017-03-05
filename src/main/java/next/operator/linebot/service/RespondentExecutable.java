package next.operator.linebot.service;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface RespondentExecutable {

  /** 語法結構(允許多種) */
  String[] structures();

  /** 執行 */
  String execute(String... args);

  default String structure() {
    return Stream.of(structures()).collect(Collectors.joining("\n"));
  }

}
