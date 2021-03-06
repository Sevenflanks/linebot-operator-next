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
   * 5: 沒有個可用的資料
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
        sb.append("這是不安全的網站！！").append("\n");
        hasRisk(sb);
        sb.append("真心建議不要點！");
        break;
      case 2:
        sb.append("未找到任何不安全的內容～");
        sb.append("請安心食用");
        break;
      case 3:
        sb.append("這個網站的部分網頁不安全").append("\n");
        hasRisk(sb);
        sb.append("人品好或防毒好的話才可以點！");
        break;
      case 4:
        sb.append("這個網站含有大量內容，無法完全保證安全").append("\n");
        hasRisk(sb);
        sb.append("裡面的網址最好也送上來讓我檢查喔！");
        break;
      case 5:
        sb.append("估狗大神好像沒有針對這個網站進行診斷").append("\n");
        hasRisk(sb);
        sb.append("閱覽有賺有賠，點下去前請仔細閱讀使用說明書");
        break;
      case 7:
        sb.append("這個網站代管的檔案不是常見的下載項目").append("\n");
        hasRisk(sb);
        sb.append("建議不要下載任何片子！");
        break;
      default:
        sb.append("估狗大神給的結果我認不得耶QQ... list_status:").append(listStatus).append("\n");
        if (hasRisk(sb)) {
          sb.append("不過看起來沒什麼風險，應該可以安心瀏覽喔");
        } else {
          sb.append("而且有一些風險項目，請酌情點閱喔！");
        }
        break;
    }

    return sb.toString();
  }

  /** 存在風險與否, 若有則加上相關風險訊息 */
  private boolean hasRisk(StringBuilder sb) {
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
      return true;
    } else {
      return false;
    }
  }
}
