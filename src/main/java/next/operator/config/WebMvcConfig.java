package next.operator.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebMvc
@Primary
@ComponentScan(basePackages = {"next.operator.**.web"}, useDefaultFilters = false, includeFilters = @Filter({Controller.class}))
public class WebMvcConfig implements WebMvcConfigurer {

	@Autowired
	private WebObjectMapper objectMapper;

	@Override
	public void configureMessageConverters(final List<HttpMessageConverter<?>> converters) {
		converters.add(new MappingJackson2HttpMessageConverter(objectMapper));
		converters.add(new ByteArrayHttpMessageConverter());
	}
}
