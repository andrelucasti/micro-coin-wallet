package com.crypto.coinwallet.andrelucas.app.portfolio;

import com.crypto.coinwallet.andrelucas.business.portfolio.PortfolioIntegration;
import com.crypto.coinwallet.andrelucas.business.portfolio.Portfolio;
import io.awspring.cloud.sns.core.SnsTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
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

        snsTemplate.convertAndSend(topicName, portfolioIntegrationDTO);
    }
}
