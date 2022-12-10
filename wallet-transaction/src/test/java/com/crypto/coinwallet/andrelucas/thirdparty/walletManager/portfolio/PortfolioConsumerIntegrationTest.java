package com.crypto.coinwallet.andrelucas.thirdparty.walletManager.portfolio;

import com.crypto.coinwallet.andrelucas.WalletTransactionApplicationTests;
import com.crypto.coinwallet.andrelucas.business.portfolio.PortfolioRepository;
import com.google.common.io.Resources;
import org.assertj.core.api.Assertions;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

class PortfolioConsumerIntegrationTest extends WalletTransactionApplicationTests {

    @Value("${consumer.portfolio.queue-name}")
    private String queueName;

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Test
    void shouldCreatePortfolioWhenReceiveFromQueue() throws IOException, URISyntaxException {
        var id = UUID.randomUUID();
        var name = "Token Wallet";
        var payload = Resources.toString(Resources.getResource("contracts/portfolio-consumer-message.json"), StandardCharsets.UTF_8)
                .replace("{id}", id.toString())
                .replace("{name}", name);
        var sendMessageRequest = SendMessageRequest
                .builder()
                .queueUrl(queueName)
                .messageBody(payload)
                .build();
        var sendMessageResponse = sqsClient().sendMessage(sendMessageRequest);

        Assertions.assertThat(sendMessageResponse.sdkHttpResponse().isSuccessful()).isTrue();
        Awaitility.await().atMost(60, TimeUnit.SECONDS).untilAsserted(()->{

            var portfolioRepositoryAll = portfolioRepository.findAll();
            var portfolioSaved = portfolioRepositoryAll.stream().findAny().get();

            Assertions.assertThat(portfolioSaved.id()).isEqualTo(id);
            Assertions.assertThat(portfolioSaved.name()).isEqualTo(name);
        });
    }
}