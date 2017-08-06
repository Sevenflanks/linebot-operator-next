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
  @DurationRange(minSeconds = 60*10)
  private Duration fixedRate;

  @Label("最後推送時間")
  @Column
  private Instant lastPushTime;

  @Label("訂閱訊息")
  @Column
  @NotNull
  @Size(min = 1, max = 255)
  private String msg;

}
