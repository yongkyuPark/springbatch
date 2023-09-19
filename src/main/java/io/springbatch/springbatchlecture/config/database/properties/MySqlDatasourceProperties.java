package io.springbatch.springbatchlecture.config.database.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.datasource.mysql")
public class MySqlDatasourceProperties extends DatasourceProperties{
}
