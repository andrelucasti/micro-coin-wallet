package com.crypto.coinwallet.andrelucas;

import java.util.Objects;
import java.util.UUID;

public class RunnableCorrelationIdWrapper implements Runnable {
    private final Runnable delegate;
    private final CorrelationIdResolver correlationIdResolver;
    private final UUID currentCorrelationId;
    private final String serviceName;

    public RunnableCorrelationIdWrapper(final Runnable delegate,
                                        final CorrelationIdResolver correlationIdResolver,
                                        final String serviceName) {
        this.serviceName = Objects.requireNonNull(serviceName);
        this.delegate = Objects.requireNonNull(delegate);
        this.correlationIdResolver = Objects.requireNonNull(correlationIdResolver);
        this.currentCorrelationId = correlationIdResolver.getCorrelationId();
    }

    @Override
    public void run() {
        Thread.currentThread().setName("runnableCorrelationIdWrapper-".concat(serviceName));
        correlationIdResolver.setCurrent(currentCorrelationId);
        delegate.run();
    }
}



