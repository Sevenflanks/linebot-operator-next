package next.operator.config;

import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"next.operator.linebot.handler"}, useDefaultFilters = false, includeFilters = @Filter({LineMessageHandler.class}))
public class LineConfig {

}
