package com.crypto.coinwallet.andrelucas.app.portfolio;

import com.crypto.coinwallet.andrelucas.CorrelationIdResolver;
import com.crypto.coinwallet.andrelucas.app.IntegrationException;
import com.crypto.coinwallet.andrelucas.business.portfolio.Portfolio;
import com.crypto.coinwallet.andrelucas.business.portfolio.PortfolioIntegration;
import io.awspring.cloud.sns.core.SnsTemplate;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class PortfolioTopicIntegration implements PortfolioIntegration {

    private final String topicName;
    private final SnsTemplate snsTemplate;
    private final CorrelationIdResolver correlationIdResolver;

    public PortfolioTopicIntegration(@Value("${integration.portfolio.topic}") final String topicName,
                                     final SnsTemplate snsTemplate,
                                     final CorrelationIdResolver correlationIdResolver) {
        this.topicName = topicName;
        this.snsTemplate = snsTemplate;
        this.correlationIdResolver = correlationIdResolver;
    }

    @Override
    public void send(Portfolio portfolio) {
        var portfolioIntegrationDTO = new PortfolioIntegrationDTO(portfolio.id(), portfolio.name());

        try {
            var correlationId = correlationIdResolver.getCorrelationId();

            log.info(String.format("Sending message to topic - %s portfolioId %s", topicName, portfolio.id()));
            snsTemplate.convertAndSend(topicName, portfolioIntegrationDTO, Map.of("correlationId", correlationId.toString()));
        } catch (Exception e) {
          String errorMsg = String.format("Got error to send the portfolio %s to topic - %s",
                  portfolio.id(),
                  topicName);

          throw new IntegrationException(errorMsg, e);
      }
    }
}
