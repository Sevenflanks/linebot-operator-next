package next.operator.subscription.entity;

import lombok.Getter;
import lombok.Setter;
import next.operator.common.persistence.GenericEntity;
import next.operator.common.validation.DurationRange;
import next.operator.common.validation.Label;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@SuppressWarnings("serial")
@Entity
@Table
@Getter
@Setter
public class Subscription extends GenericEntity {

  @Label("訂閱者")
  @Embedded
  @NotNull
  private Subscriber subscriber;

  @Label("開始時間")
  @Column
  @NotNull
  private Instant startTime;

  @Label("間隔時間")
  @Column
  @NotNull
  @DurationRange(minSeconds = 10, maxSeconds = 7 * 24 * 60 * 60)
  private Duration fixedRate;

  @Label("最後推送時間")
  @Column
  private Instant lastPushTime;

  @Label("訂閱訊息")
  @Column
  @NotNull
  @Size(min = 1, max = 255)
  private String msg;

  @Override
  public String toString() {
    return "ID:" + id + ")於" +
        DateTimeFormatter.ISO_ZONED_DATE_TIME.format(startTime.atOffset(ZoneOffset.ofHours(8))) +
        "開始，每" + fixedRate.getSeconds() + "秒執行一次:【" + msg + "】";
  }
}
