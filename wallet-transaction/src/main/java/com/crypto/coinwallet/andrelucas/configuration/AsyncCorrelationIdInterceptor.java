package com.crypto.coinwallet.andrelucas.configuration;

import com.crypto.coinwallet.andrelucas.configuration.correlationId.CorrelationIdResolver;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.listener.interceptor.MessageInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;

import java.util.UUID;


@Slf4j
public class AsyncCorrelationIdInterceptor implements MessageInterceptor<Object> {
    private final ObjectMapper objectMapper;
    private final CorrelationIdResolver correlationIdResolver;

    public AsyncCorrelationIdInterceptor(final ObjectMapper objectMapper,
                                         final CorrelationIdResolver correlationIdResolver) {
        this.objectMapper = objectMapper;
        this.correlationIdResolver = correlationIdResolver;
    }

    @Override
    public Message<Object> intercept(final Message<Object> message) {
        try{
            var messageAttributes = objectMapper
                                        .readTree(message.getPayload().toString())
                                        .get("MessageAttributes");
            var correlationId = messageAttributes.get("correlationId").get("Value");
            correlationIdResolver.setCurrent(UUID.fromString(correlationId.textValue()));
            return MessageInterceptor.super.intercept(message);
        }catch (Exception e){
            log.error("Got error to get correlationId", e);
            throw new RuntimeException(e);
        }
    }
}








