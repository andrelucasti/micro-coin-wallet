package com.crypto.coinwallet.andrelucas.configuration;

import com.crypto.coinwallet.andrelucas.configuration.correlationId.CorrelationIdResolver;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.config.SqsMessageListenerContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Configuration
public class ConsumerConfiguration {
    private final ObjectMapper objectMapper;
    private final CorrelationIdResolver correlationIdResolver;

    public ConsumerConfiguration(final ObjectMapper objectMapper,
                                 final CorrelationIdResolver correlationIdResolver) {
        this.objectMapper = objectMapper;
        this.correlationIdResolver = correlationIdResolver;
    }

    @Bean
    SqsMessageListenerContainerFactory<Object> defaultSqsListenerContainerFactory(SqsAsyncClient sqsAsyncClient){
        return SqsMessageListenerContainerFactory.builder()
                .messageInterceptor(new AsyncCorrelationIdInterceptor(objectMapper, correlationIdResolver))
                .sqsAsyncClient(sqsAsyncClient)
                .build();
    }
}
