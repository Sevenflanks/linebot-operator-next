package next.operator.config;

import com.linecorp.bot.client.ChannelTokenSupplier;
import com.linecorp.bot.client.FixedChannelTokenSupplier;
import com.linecorp.bot.client.LineBlobClient;
import com.linecorp.bot.client.LineClientConstants;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.client.LineSignatureValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;

@Slf4j
@Configuration
public class LineConfig {

    @Value("${line.bot.channelToken}")
    private String channelToken;
    @Value("${line.bot.channelSecret}")
    private String channelSecret;
    @Value("${line.bot.connectTimeout}")
    private long connectTimeout;
    @Value("${line.bot.readTimeout}")
    private long readTimeout;
    @Value("${line.bot.writeTimeout}")
    private long writeTimeout;

    @Bean
    public FixedChannelTokenSupplier channelTokenSupplier() {
        return FixedChannelTokenSupplier.of(channelToken);
    }

    @Bean
    public LineSignatureValidator lineSignatureValidator() {
        return new LineSignatureValidator(channelSecret.getBytes(StandardCharsets.US_ASCII));
    }

    @Bean
    public LineMessagingClient lineMessagingClient(FixedChannelTokenSupplier channelTokenSupplier) {
        return LineMessagingClient
            .builder(channelTokenSupplier)
            .apiEndPoint(LineClientConstants.DEFAULT_API_END_POINT)
            .blobEndPoint(LineClientConstants.DEFAULT_BLOB_END_POINT)
            .connectTimeout(connectTimeout)
            .readTimeout(readTimeout)
            .writeTimeout(writeTimeout)
            .build();
    }

    @Bean
    public LineBlobClient lineBlobClient(final ChannelTokenSupplier channelTokenSupplier) {
        return LineBlobClient
            .builder(channelTokenSupplier)
            .apiEndPoint(LineClientConstants.DEFAULT_BLOB_END_POINT)
            .connectTimeout(connectTimeout)
            .readTimeout(readTimeout)
            .writeTimeout(writeTimeout)
            .build();
    }

}
