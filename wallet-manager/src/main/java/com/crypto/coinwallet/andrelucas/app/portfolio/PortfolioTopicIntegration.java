package com.crypto.coinwallet.andrelucas.app.portfolio;

import com.crypto.coinwallet.andrelucas.app.IntegrationException;
import com.crypto.coinwallet.andrelucas.business.portfolio.Portfolio;
import com.crypto.coinwallet.andrelucas.business.portfolio.PortfolioIntegration;
import io.awspring.cloud.sns.core.SnsTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class PortfolioTopicIntegration implements PortfolioIntegration {

    private final String topicName;
    private final SnsTemplate snsTemplate;

    public PortfolioTopicIntegration(@Value("${integration.portfolio.topic}") final String topicName,
                                     final SnsTemplate snsTemplate) {
        this.topicName = topicName;
        this.snsTemplate = snsTemplate;
    }

    @Override
    public void send(Portfolio portfolio) {
        var portfolioIntegrationDTO = new PortfolioIntegrationDTO(portfolio.id(), portfolio.name());

        try {
          log.info(String.format("Sending message to topic - %s portfolioId %s", topicName, portfolio.id()));
          snsTemplate.convertAndSend(topicName, portfolioIntegrationDTO);
      }catch (Exception e){
          String errorMsg = String.format("Got error to send the portfolio %s  to topic - %s",
                  portfolio.id(),
                  topicName);

          throw new IntegrationException(errorMsg, e);
      }
    }
}
