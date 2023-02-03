package com.crypto.coinwallet.andrelucas.thirdparty.walletManager.portfolio;

import com.crypto.coinwallet.andrelucas.business.portfolio.Portfolio;
import com.crypto.coinwallet.andrelucas.business.portfolio.PortfolioRepository;
import com.crypto.coinwallet.andrelucas.thirdparty.walletManager.ConsumerException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PortfolioConsumer {
    private final ObjectMapper objectMapper;
    private final PortfolioRepository portfolioRepository;
    public PortfolioConsumer(final ObjectMapper objectMapper,
                             final PortfolioRepository portfolioRepository) {
        this.objectMapper = objectMapper;
        this.portfolioRepository = portfolioRepository;
    }
    @SqsListener(value = "${consumer.portfolio.queue-name}")
    public void consume(String message){
        try {
            var body = objectMapper.readTree(message).get("Message").textValue();
            var portfolioConsumerDTO = objectMapper.readValue(body, PortfolioConsumerDTO.class);

            portfolioRepository.save(new Portfolio(portfolioConsumerDTO.id(), portfolioConsumerDTO.name()));

            log.info(String.format("receive message portfolio: id  %s", portfolioConsumerDTO.id()));
        } catch (Throwable e){
            var errorMsg = "got error at consumer portfolioDTO";
            throw new ConsumerException(errorMsg, e);
        }
    }
    @MessageExceptionHandler(value = ConsumerException.class)
    void handlerException(final ConsumerException e){
        log.error("handlerException", e);
    }
}

