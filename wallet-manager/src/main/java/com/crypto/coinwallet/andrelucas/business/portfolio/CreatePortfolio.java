package com.crypto.coinwallet.andrelucas.business.portfolio;

import org.springframework.stereotype.Service;

@Service
public class CreatePortfolio {
    private final PortfolioBusinessRepository portfolioRepository;
    private final PortfolioIntegration portfolioIntegration;

    public CreatePortfolio(final PortfolioBusinessRepository portfolioRepository,
                           final PortfolioIntegration portfolioIntegration) {
        this.portfolioRepository = portfolioRepository;
        this.portfolioIntegration = portfolioIntegration;
    }

    public void execute(Portfolio portfolio){
        portfolioRepository.save(portfolio);

        //TODO must be async and when transaction to be committed
        var newPortfolio = portfolioRepository
                .findByUserIdAndName(portfolio.userId(), portfolio.name())
                .orElseThrow(PortfolioNotFoundException::new);

        portfolioIntegration.send(newPortfolio);
    }
}
