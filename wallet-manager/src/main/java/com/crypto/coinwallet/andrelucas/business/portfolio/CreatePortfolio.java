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
        portfolioIntegration.send(portfolio);
    }
}
