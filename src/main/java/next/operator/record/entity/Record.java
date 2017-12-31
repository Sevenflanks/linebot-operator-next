package next.operator.record.entity;

import next.operator.common.persistence.GenericEntity;

import java.time.LocalDateTime;

/** 說話紀錄 */
public class Record extends GenericEntity {

  private String key;
  private String talkedTo; // senderId
  private LocalDateTime talkedTime;

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getTalkedTo() {
    return talkedTo;
  }

  public void setTalkedTo(String talkedTo) {
    this.talkedTo = talkedTo;
  }

  public LocalDateTime getTalkedTime() {
    return talkedTime;
  }

  public void setTalkedTime(LocalDateTime talkedTime) {
    this.talkedTime = talkedTime;
  }
}
