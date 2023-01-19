package com.crypto.coinwallet.andrelucas.business;

import com.crypto.coinwallet.andrelucas.CorrelationIdResolver;
import com.crypto.coinwallet.andrelucas.RunnableCorrelationIdWrapper;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;

@Service
public class AsyncService {
    private final ExecutorService executorVirtualService;
    private final CorrelationIdResolver correlationIdResolver;

    public AsyncService(final ExecutorService executorVirtualService,
                        final CorrelationIdResolver correlationIdResolver) {
        this.executorVirtualService = executorVirtualService;
        this.correlationIdResolver = correlationIdResolver;
    }

    public void execute(final Runnable runnable){
        var runnableCorrelationIdWrapper = new RunnableCorrelationIdWrapper(runnable, correlationIdResolver);
        executorVirtualService.submit(runnableCorrelationIdWrapper);
    }
}