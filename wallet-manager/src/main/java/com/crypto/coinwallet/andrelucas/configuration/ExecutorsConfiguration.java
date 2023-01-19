package com.crypto.coinwallet.andrelucas.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ExecutorsConfiguration {

    @Bean
    public ExecutorService executorVirtualService(){
        return Executors.newVirtualThreadPerTaskExecutor();
    }
}
