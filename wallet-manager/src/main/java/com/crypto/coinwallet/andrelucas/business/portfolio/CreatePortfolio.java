package com.crypto.coinwallet.andrelucas.business.portfolio;

import com.crypto.coinwallet.andrelucas.business.AsyncService;
import org.springframework.stereotype.Service;

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

    //TODO should run rollback if happens an error at to send to Topic.
    //TODO should run rollback if happens an error at to save and should not send to the topic.
    public void execute(Portfolio portfolio){
        portfolioRepository.save(portfolio);

        var newPortfolio = portfolioRepository
                .findByUserIdAndName(portfolio.userId(), portfolio.name())
                .orElseThrow(PortfolioNotFoundException::new);

        asyncService.execute(() ->  portfolioIntegration.send(newPortfolio));
    }
}
