package io.springbatch.springbatchlecture.config.database;

import com.zaxxer.hikari.HikariDataSource;
import io.springbatch.springbatchlecture.annotation.MysqlRepository;
import io.springbatch.springbatchlecture.config.database.properties.MySqlDatasourceProperties;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableConfigurationProperties(MySqlDatasourceProperties.class)
@EnableJpaRepositories(
        basePackages = "io.springbatch.springbatchlecture.aTest.repository",
        entityManagerFactoryRef = "mysqlEntityManagerFactory",
        transactionManagerRef = "mysqlTransactionManager"
)
public class MysqlDatabaseConfig {

    @Primary
    @Bean(name = "mysqlDatasource")
    @ConfigurationProperties(prefix = "spring.datasource.mysql")
    public DataSource mysqlDatasource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Primary
    @Bean(name = "mysqlTransactionManager")
    public PlatformTransactionManager mysqlTransactionManager(@Qualifier("mysqlDatasource") DataSource dataSource) {
        //return new DataSourceTransactionManager(dataSource);
        EntityManagerFactory object = mysqlEntityManagerFactory(dataSource).getObject();
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager(object);
        return jpaTransactionManager;
    }

    @Primary
    @Bean(name = "mysqlEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean mysqlEntityManagerFactory(@Qualifier("mysqlDatasource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);

        //Entity 패키지 경로
        em.setPackagesToScan("io.springbatch.springbatchlecture.aTest.entity");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        //Hibernate 설정
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect","org.hibernate.dialect.MySQL8Dialect");
        properties.put("hibernate.physical_naming_strategy","org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy");
        em.setJpaPropertyMap(properties);

        em.setPersistenceUnitName("mysqlUnit");

        return em;
    }

}
