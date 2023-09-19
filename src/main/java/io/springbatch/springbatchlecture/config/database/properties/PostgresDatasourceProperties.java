package io.springbatch.springbatchlecture.config.database.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.datasource.postgresql")
public class PostgresDatasourceProperties extends DatasourceProperties{
}
