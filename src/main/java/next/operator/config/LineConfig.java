package next.operator.config;

import com.linecorp.bot.spring.boot.LineBotAutoConfiguration;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ComponentScan(basePackages = {"next.operator.linebot.handler"}, useDefaultFilters = false, includeFilters = @Filter({LineMessageHandler.class}))
public class LineConfig extends LineBotAutoConfiguration {

}
