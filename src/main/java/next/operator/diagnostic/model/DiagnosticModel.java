package next.operator.diagnostic.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@Setter
public class DiagnosticModel {

  /**
   * 1: 這是不安全的網站
   * 2: 未找到任何不安全的內容
   * 3: 這個網站的部分網頁不安全
   * 4: 這個網站含有大量內容，無法完全保證安全
   * 7: 這個網站代管的檔案不是常見的下載項目
   */
  @JsonProperty("list_status")
  private int listStatus;

  /** 將訪客導向有害網站 */
  @JsonProperty("is_malware")
  private boolean malware;

  /** 在訪客的電腦上安裝垃圾或惡意軟體 */
  @JsonProperty("is_unwanted")
  private boolean unwanted; //

  /** 企圖誘騙訪客提供個人資訊或下載軟體 */
   @JsonProperty("is_social")
  private boolean social;

  /** 包還惡意軟體 */
  @JsonProperty("is_malware_download")
  private boolean malwareDownload;

  /** 包含垃圾軟體 */
  @JsonProperty("is_unwanted_download")
  private boolean unwantedDownload;

  /** 包含可疑或不明軟體 */
  @JsonProperty("is_unknown_download")
  private boolean unknownDownload;

  /** 網站名稱 */
  @JsonProperty("site_name")
  private String siteName;

  /** 最後更新日期時間(秒) */
  @JsonProperty("data_last_updated_date")
  private int dataLastUpdatedDate;

  public LocalDateTime getDataLastUpdatedDate() {
    return LocalDateTime.ofInstant(Instant.ofEpochSecond(dataLastUpdatedDate), ZoneId.systemDefault());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("檢查的結果，發現");
    switch (listStatus) {
      case 1:
        sb.append("這是不安全的網站").append("\n");
        risk(sb);
        sb.append("強烈建議不要點！");
        break;
      case 2:
        sb.append("未找到任何不安全的內容～");
        sb.append(" 請安心食用");
        break;
      case 3:
        sb.append("這個網站的部分網頁不安全").append("\n");
        risk(sb);
        sb.append("人品好或防毒好的話才可以點！");
        break;
      case 4:
        sb.append("這個網站含有大量內容，無法完全保證安全").append("\n");
        risk(sb);
        sb.append("裡面的網址建議也送上來讓我檢查喔！");
        break;
      case 7:
        sb.append("這個網站代管的檔案不是常見的下載項目").append("\n");
        risk(sb);
        sb.append("建議不要下載任何片子！");
        break;
      default:
        sb.append("估狗大神給的結果我認不得QQ list_status:").append(listStatus);
        risk(sb);
        sb.append("不太清楚危不危險QQ");
        break;
    }

    return super.toString();
  }

  private void risk(StringBuilder sb) {
    if (malware || unwanted || social || malwareDownload || unwantedDownload || unknownDownload) {
      sb.append("可能的風險為以下").append("\n");
      if (malware) {
        sb.append("‧將訪客導向有害網站").append("\n");
      }
      if (unwanted) {
        sb.append("‧在訪客的電腦上安裝垃圾或惡意軟體").append("\n");
      }
      if (social) {
        sb.append("‧企圖誘騙訪客提供個人資訊或下載軟體").append("\n");
      }
      if (malwareDownload) {
        sb.append("‧包還惡意軟體").append("\n");
      }
      if (unwantedDownload) {
        sb.append("‧包含垃圾軟體").append("\n");
      }
      if (unknownDownload) {
        sb.append("‧包含可疑或不明軟體").append("\n");
      }
    }
  }
}
