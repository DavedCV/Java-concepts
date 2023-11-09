package com.example.jdbctemplatepersisteddata.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = "com.example.jdbctemplatepersisteddata")
@PropertySource("classpath:database.properties")
public class ProjectConfiguration {

    @Value("${url}")
    private String URL;
    @Value("${dbuser}")
    private String USER;
    @Value("${dbpassword}")
    private String PASSWORD;

    // The method returns a DataSource object. If
    // Spring Boot finds a DataSource already exists in
    // the Spring context it doesn’t configure one.
    @Bean
    DataSource dataSource() {
        // We’ll use HikariCP as the data source implementation
        // for this example. However, when you define the bean
        // yourself, you can choose other implementations if
        // your project requires something else.
        HikariDataSource dataSource = new HikariDataSource();

        // We set the connection parameters on the data source.
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USER);
        dataSource.setPassword(PASSWORD);
        dataSource.setConnectionTimeout(1000);

        // We return the DataSource instance, and Spring adds it to its context.
        return dataSource;
    }
}
