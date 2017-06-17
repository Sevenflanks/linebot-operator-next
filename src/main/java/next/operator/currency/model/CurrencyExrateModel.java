package next.operator.currency.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.ValidationException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CurrencyExrateModel {

  private String exFrom;

  private String exTo;

  private LocalDateTime time;

  private BigDecimal exrate;

  /** 反轉匯率 */
  public CurrencyExrateModel reverse() {
    final CurrencyExrateModel reversion = new CurrencyExrateModel();
    reversion.setExFrom(this.exTo);
    reversion.setExTo(this.exFrom);
    reversion.setTime(this.time);
    reversion.setExrate(BigDecimal.ONE.divide(this.exrate, 6, BigDecimal.ROUND_HALF_UP));
    return reversion;
  }

  /** 合併匯率 */
  public CurrencyExrateModel merge(CurrencyExrateModel next) {
    final CurrencyExrateModel merged = new CurrencyExrateModel();
    if (!this.exTo.equals(next.exFrom)) {
      throw new ValidationException("這兩個匯率檔無法合併哦，幫我叫主人回家檢查程式。[left:" + this + ", right:" + next + "]");
    } else {
      merged.exFrom = this.exFrom;
      merged.exTo = next.exTo;
      merged.time = this.time.compareTo(next.time) > 0 ? next.time : this.time;
      merged.exrate = this.exrate.multiply(next.exrate);
    }
    return merged;
  }

}
