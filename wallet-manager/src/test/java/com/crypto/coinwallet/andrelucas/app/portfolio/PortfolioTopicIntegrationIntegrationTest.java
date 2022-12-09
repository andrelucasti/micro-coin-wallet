package com.crypto.coinwallet.andrelucas.app.portfolio;

import com.crypto.coinwallet.andrelucas.WalletManagerApplicationTests;
import com.crypto.coinwallet.andrelucas.business.portfolio.Portfolio;
import org.assertj.core.api.Assertions;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.services.sqs.model.Message;

import java.net.URISyntaxException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

class PortfolioTopicIntegrationIntegrationTest extends WalletManagerApplicationTests {

    @Autowired
    private PortfolioTopicIntegration portfolioTopicIntegration;
    private @Value("${integration.portfolio.topic}") String topicName;
    @Test
    void shouldSendToTopic() throws URISyntaxException {
        var queueName = "wallet-test";
        var subscribeRequest = createSubscribe(topicName, queueName);

        var userId = UUID.randomUUID();
        var id = UUID.randomUUID();
        var name = "CryptoWallet";
        var portfolio = new Portfolio(name, userId, id);

        portfolioTopicIntegration.send(portfolio);

        Awaitility.await().atMost(60, TimeUnit.SECONDS).untilAsserted(() ->{
            var receiveMessageResponse = receiveMessage(subscribeRequest.endpoint());
            Assertions.assertThat(receiveMessageResponse.hasMessages()).isTrue();

        });
        Awaitility.await().atMost(60, TimeUnit.SECONDS).untilAsserted(()->{
            var receiveMessageResponse = receiveMessage(subscribeRequest.endpoint());
            Optional<PortfolioIntegrationDTO> optionalPortfolioDTO = receiveMessageResponse.messages().stream().findAny()
                    .map(Message::body)
                    .map(message -> convertFromSNSPayloadToObject(message, PortfolioIntegrationDTO.class));
            Assertions.assertThat(optionalPortfolioDTO).isNotEmpty();
            PortfolioIntegrationDTO portfolioDTO = optionalPortfolioDTO.get();

            Assertions.assertThat(portfolioDTO.id()).isEqualTo(id);
            Assertions.assertThat(portfolioDTO.name()).isEqualTo(name);
        });
    }
}