package next.operator.subscription;

import lombok.extern.slf4j.Slf4j;
import next.operator.GenericTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;

import java.sql.Date;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Slf4j
public class TestJob extends GenericTest {

  @Autowired
  private TaskScheduler taskScheduler;

  @Test
  public void test() throws InterruptedException {
    taskScheduler.scheduleAtFixedRate(() -> {
      log.info("Now:{}", LocalDateTime.now());
    }, Date.from(Instant.now()), Duration.ofSeconds(1).toMillis());
    taskScheduler.scheduleAtFixedRate(() -> {
      log.info("Fix-0-NANO:{}", LocalDateTime.now());
    }, Date.from(LocalDateTime.now().plusSeconds(1).withNano(0).toInstant(ZoneOffset.ofHours(8))), Duration.ofSeconds(1).toMillis());

    Thread.sleep(Duration.ofSeconds(10).toMillis());
  }

  public static void main(String[] args) {
    System.out.println(Duration.ofSeconds(1).toMillis());
  }

}
