package com.crypto.coinwallet.andrelucas.business.portfolio;

import com.crypto.coinwallet.andrelucas.business.AsyncService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreatePortfolio {
    private final PortfolioBusinessRepository portfolioRepository;
    private final PortfolioIntegration portfolioIntegration;
    private final AsyncService asyncService;

    public CreatePortfolio(final PortfolioBusinessRepository portfolioRepository,
                           final PortfolioIntegration portfolioIntegration,
                           final AsyncService asyncService) {

        this.portfolioRepository = portfolioRepository;
        this.portfolioIntegration = portfolioIntegration;
        this.asyncService = asyncService;
    }

    @Transactional
    public void execute(Portfolio portfolio){
        portfolioRepository.save(portfolio);

        var newPortfolio = portfolioRepository.findByUserIdAndName(portfolio.userId(), portfolio.name())
                .orElseThrow(PortfolioNotFoundException::new);

        asyncService.execute("sendPortfolioToTopic", () -> portfolioIntegration.send(newPortfolio));
    }
}

