package com.gca.config;

import com.gca.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.apache.tomcat.jdbc.pool.DataSource;

import java.util.Properties;

@Configuration
@ComponentScan(basePackages = "com.gca")
@PropertySource(value = "classpath:application.yml", factory = YamlPropertySourceFactory.class)
public class PersistenceConfig {

    private static final boolean SHOW_SQL = true;
    private static final boolean FORMAT_SQL = true;

    private static final String PROP_JDBC_DRIVER = "jakarta.persistence.jdbc.driver";
    private static final String PROP_JDBC_URL = "jakarta.persistence.jdbc.url";
    private static final String PROP_JDBC_USER = "jakarta.persistence.jdbc.user";
    private static final String PROP_JDBC_PASSWORD = "jakarta.persistence.jdbc.password";

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Value("${spring.datasource.driver-class-name}")
    private String dbDriver;

    @Value("${spring.jpa.database-platform}")
    private String hibernateDialect;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddlAuto;

    @Bean
    public DataSource dataSource() {
        DataSource ds = new DataSource();
        ds.setDriverClassName(dbDriver);
        ds.setUrl(dbUrl);
        ds.setUsername(dbUsername);
        ds.setPassword(dbPassword);
        return ds;
    }

    @Bean
    public SessionFactory sessionFactory() {
        Properties settings = hibernateProperties();

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(settings)
                .build();

        MetadataSources sources = new MetadataSources(serviceRegistry)
                .addAnnotatedClass(User.class);

        Metadata metadata = sources.buildMetadata();

        return metadata.buildSessionFactory();
    }

    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put(PROP_JDBC_DRIVER, dbDriver);
        properties.put(PROP_JDBC_URL, dbUrl);
        properties.put(PROP_JDBC_USER, dbUsername);
        properties.put(PROP_JDBC_PASSWORD, dbPassword);
        properties.put(Environment.DIALECT, hibernateDialect);
        properties.put(Environment.HBM2DDL_AUTO, ddlAuto);
        properties.put(Environment.SHOW_SQL, String.valueOf(SHOW_SQL));
        properties.put(Environment.FORMAT_SQL, String.valueOf(FORMAT_SQL));
        return properties;
    }
}
