package com.crypto.coinwallet.andrelucas.configuration.correlationId;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.UUID;
@Component
public class CorrelationIdResolver {
    private static final String CORRELATION_ID = "correlationId";

    private final ThreadLocal<UUID> current = new ThreadLocal<>();

    public void setCurrent(final UUID correlationId){
        current.set(correlationId);
        MDC.put(CORRELATION_ID, correlationId.toString());
    }

    public UUID getCorrelationId(){
        return current.get();
    }
}
