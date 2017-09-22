package next.operator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

@Configuration
@ComponentScan(basePackages = {
    "next.operator.scheduled"
})
public class SchedulingConfig {

  @Bean
  public TaskScheduler taskScheduler() {
    final ConcurrentTaskScheduler taskScheduler = new ConcurrentTaskScheduler();
    return taskScheduler;
  }

}
