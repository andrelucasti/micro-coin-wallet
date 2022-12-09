package com.crypto.walletmanager.business.portfolio;

import org.springframework.stereotype.Service;

@Service
public class CreatePortfolio {
    private final PortfolioBusinessRepository portfolioRepository;

    public CreatePortfolio(PortfolioBusinessRepository portfolioRepository) {
        this.portfolioRepository = portfolioRepository;
    }

    public void execute(Portfolio portfolio){
        portfolioRepository.save(portfolio);
    }
}
