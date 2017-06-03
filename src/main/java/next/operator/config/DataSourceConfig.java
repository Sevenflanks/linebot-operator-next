package next.operator.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

//@Configuration
//@EnableJpaRepositories
//@EnableTransactionManagement
public class DataSourceConfig {

  @Bean
  @Primary
  public DataSourceProperties dataSourceProperties() {
    return new DataSourceProperties();
  }

  @Bean
  public DataSource dataSource(DataSourceProperties properties) {
    return properties.initializeDataSourceBuilder()
        .type(HikariDataSource.class).build();
  }

}
