package com.crypto.walletmanager.dataprovider.portfolio;

import com.crypto.walletmanager.business.portfolio.Portfolio;
import org.springframework.stereotype.Component;

@Component
public class PortfolioConverter {

    public Portfolio convertFromEntityToModel(PortfolioEntity portfolioEntity) {
        return new Portfolio(portfolioEntity.getName(), portfolioEntity.getUserId(), portfolioEntity.getId());
    }

}
