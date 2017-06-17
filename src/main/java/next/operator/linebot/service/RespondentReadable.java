package next.operator.linebot.service;

/**
 * 原生語言介面
 */
public interface RespondentReadable {

  /** 看懂否 */
  boolean isReadable(String message);

  /** 執行 */
  String talk(String message);
}
