package next.operator.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
    "next.operator.scheduled"
})
public class SchedulingConfig {

}