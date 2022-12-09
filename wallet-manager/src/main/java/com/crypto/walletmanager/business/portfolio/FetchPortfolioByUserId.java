package com.crypto.walletmanager.business.portfolio;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FetchPortfolioByUserId {
    private final PortfolioBusinessRepository portfolioRepository;

    public FetchPortfolioByUserId(PortfolioBusinessRepository portfolioRepository) {
        this.portfolioRepository = portfolioRepository;
    }

    public List<Portfolio> execute(UUID userId){
        return this.portfolioRepository.findBy(userId);
    }
}