package next.operator.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(
    basePackages = "next.operator.**.dao"
)
@EnableTransactionManagement
public class DataSourceConfig {

//  @Bean
//  @Primary
//  public DataSourceProperties dataSourceProperties() {
//    return new DataSourceProperties();
//  }

//  @Bean
//  public DataSource dataSource(DataSourceProperties properties) {
//    return properties.initializeDataSourceBuilder()
//        .type(HikariDataSource.class).build();
//  }

}
