package next.operator.subscription.entity;

import lombok.Getter;
import lombok.Setter;
import next.operator.common.persistence.GenericEmbeddedEntity;
import next.operator.common.validation.Label;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@SuppressWarnings("serial")
@Embeddable
@Getter
@Setter
public class Subscriber extends GenericEmbeddedEntity {

  @Label("訂閱者ID")
  @Column(length = 40)
  @NotNull
  private String subscriberId;

  @Label("訂閱者")
  @Column(length = 50)
  @NotNull
  private String subscriberName;

  @Label("訂閱給誰")
  @Column(length = 40)
  @NotNull
  private String subscribeTo;

}
