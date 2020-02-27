package next.operator.common.persistence;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@SuppressWarnings("serial")
@MappedSuperclass
@EntityListeners({EntityListener.class})
@Getter
@Setter
public abstract class GenericEntity implements Persistable<Long> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(updatable = false)
  protected Long id;

  @Column(updatable = false)
  protected LocalDateTime createTime;

  @Column
  protected LocalDateTime modifyTime;

  @Override
  public boolean isNew() {
    return id == null;
  }

  @Override
  public String toString() {
    return String.format("Entity of type %s with id: %s", this.getClass().getName(), getId());
  }

}
