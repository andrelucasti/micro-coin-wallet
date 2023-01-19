package com.crypto.coinwallet.andrelucas;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Component
public class CorrelationIdInterceptor implements HandlerInterceptor {
    private final CorrelationIdResolver correlationIdResolver;

    public CorrelationIdInterceptor(final CorrelationIdResolver correlationIdResolver) {
        this.correlationIdResolver = correlationIdResolver;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        correlationIdResolver.setCurrent(UUID.randomUUID());
        return true;
    }
}
