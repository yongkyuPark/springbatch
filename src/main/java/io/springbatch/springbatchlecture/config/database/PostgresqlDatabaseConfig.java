package io.springbatch.springbatchlecture.config.database;

import com.zaxxer.hikari.HikariDataSource;
import io.springbatch.springbatchlecture.config.database.properties.PostgresDatasourceProperties;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableConfigurationProperties(PostgresDatasourceProperties.class)
@EnableJpaRepositories(
        basePackages = "io.springbatch.springbatchlecture.bTest.repository",
        entityManagerFactoryRef = "postgresqlEntityManagerFactory",
        transactionManagerRef = "postgresqlTransactionManager"
)
public class PostgresqlDatabaseConfig {

    @Bean(name = "postgresqlDatasource")
    @ConfigurationProperties(prefix = "spring.datasource.postgresql")
    public DataSource postgresqlDatasource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean(name = "postgresqlTransactionManager")
    public PlatformTransactionManager postgresqlTransactionManager(@Qualifier("postgresqlDatasource") DataSource dataSource) {
        //return new DataSourceTransactionManager(dataSource);
        EntityManagerFactory object = postgresqlEntityManagerFactory(dataSource).getObject();
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager(object);
        return jpaTransactionManager;
    }

    @Bean(name = "postgresqlEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean postgresqlEntityManagerFactory(@Qualifier("postgresqlDatasource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);

        //Entity 패키지 경로
        em.setPackagesToScan("io.springbatch.springbatchlecture.bTest.entity");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        //Hibernate 설정
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect","org.hibernate.dialect.PostgreSQLDialect");
        properties.put("hibernate.physical_naming_strategy","org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy");
        properties.put("javax.persistence.schema-generation.database.action", "none");
        properties.put("hibernate.show_sql", "true");
        em.setJpaPropertyMap(properties);

        em.setPersistenceUnitName("postgresqlUnit");

        return em;
    }

}
