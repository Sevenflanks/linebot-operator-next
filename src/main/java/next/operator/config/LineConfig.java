package next.operator.config;

import lombok.extern.slf4j.Slf4j;
import next.operator.linebot.handler.LineHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("prod")
@Slf4j
@Configuration
public class LineConfig {

    @Bean
    public LineHandler lineHandler() {
        return new LineHandler();
    }

}
