package com.crypto.coinwallet.andrelucas.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {"com.crypto.coinwallet.andrelucas.dataprovider"})
public class DataSourceConfiguration {

    @Bean
    @ConfigurationProperties(value = "spring.datasource.hikari")
    DataSource dataSource(){
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }
}


