package next.operator.common.persistence;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Slf4j
public class EntityListener {

  @PrePersist
  public void onPrePersist(GenericEntity entity) {
    entity.setCreateTime(LocalDateTime.now());
  }

  @PreUpdate
  public void onPreUpdate(GenericEntity entity) {
    log.info("updating entity of {} id:{}", entity.getClass().getName(), entity.getId());
    entity.setModifyTime(LocalDateTime.now());
  }

  @PreRemove
  public void onPreRemove(GenericEntity entity) {
    log.info("removing entity of {} id:{}", entity.getClass().getName(), entity.getId());
  }

}
