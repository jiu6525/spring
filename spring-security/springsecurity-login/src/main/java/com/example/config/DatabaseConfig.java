package com.example.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {
    @Bean
    public DataSource dataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("oracle.jdbc.driver.OracleDriver");
        dataSourceBuilder.username("c##planetime");
        dataSourceBuilder.password("tiger");
        dataSourceBuilder.url("jdbc:oracle:thin:@localhost:1521/xe");
        return dataSourceBuilder.build();
    }
}