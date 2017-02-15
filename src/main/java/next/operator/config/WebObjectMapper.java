package next.operator.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


@SuppressWarnings("serial")
public class WebObjectMapper extends ObjectMapper {

    public WebObjectMapper() {
        super();
        disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        disable(SerializationFeature.INDENT_OUTPUT);  // pretty string
        enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

        registerModule(new Jdk8Module());
        registerModule(new JavaTimeModule());
    }

}
