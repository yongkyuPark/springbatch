package io.springbatch.springbatchlecture.config.database.properties;

import lombok.Data;

@Data
public abstract class DatasourceProperties {
    private String url;
    private String username;
    private String password;
    private String driverClassName;
}
