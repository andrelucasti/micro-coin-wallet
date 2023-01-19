package com.crypto.coinwallet.andrelucas;

import java.util.Objects;
import java.util.UUID;

public class RunnableCorrelationIdWrapper implements Runnable {
    private final Runnable delegate;
    private final CorrelationIdResolver correlationIdResolver;
    private final UUID currentCorrelationId;

    public RunnableCorrelationIdWrapper(final Runnable delegate,
                                        final CorrelationIdResolver correlationIdResolver) {
        this.delegate = Objects.requireNonNull(delegate);
        this.correlationIdResolver = Objects.requireNonNull(correlationIdResolver);
        this.currentCorrelationId = correlationIdResolver.getCorrelationId();
    }

    @Override
    public void run() {
        correlationIdResolver.setCurrent(currentCorrelationId);
        delegate.run();
    }
}
