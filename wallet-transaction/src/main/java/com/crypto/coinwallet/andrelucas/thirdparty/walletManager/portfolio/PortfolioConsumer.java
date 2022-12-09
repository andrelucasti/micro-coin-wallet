package com.crypto.coinwallet.andrelucas.thirdparty.walletManager.portfolio;

import com.crypto.coinwallet.andrelucas.thirdparty.walletManager.ConsumerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;

@Slf4j
public class PortfolioConsumer {
    public void consumer(final String message,
                         @Header("MessageId") final String senderId){

        try {

            System.out.println(message);

            log.info(String.format("Message received - MessageId: %s QueueName: %s", senderId, "name"));

        } catch (Throwable e){
            var errorMsg = String.format("got error at consumer portfolioDTO - SenderId: %s - message: %s", senderId, message);
            throw new ConsumerException(errorMsg, e);
        }
    }

    @MessageExceptionHandler(value = ConsumerException.class)
    void handlerException(final ConsumerException e){
        log.error("handlerException", e);
    }
}
