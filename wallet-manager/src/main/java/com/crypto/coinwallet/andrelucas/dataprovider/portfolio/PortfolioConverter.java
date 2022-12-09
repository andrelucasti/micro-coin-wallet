package com.crypto.coinwallet.andrelucas.dataprovider.portfolio;

import com.crypto.coinwallet.andrelucas.business.portfolio.Portfolio;
import org.springframework.stereotype.Component;

@Component
public class PortfolioConverter {

    public Portfolio convertFromEntityToModel(PortfolioEntity portfolioEntity) {
        return new Portfolio(portfolioEntity.getName(), portfolioEntity.getUserId(), portfolioEntity.getId());
    }

}
