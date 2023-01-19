package com.crypto.coinwallet.andrelucas.dataprovider.portfolio;

import com.crypto.coinwallet.andrelucas.business.portfolio.Portfolio;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PortfolioCrudRepository extends CrudRepository<PortfolioEntity, UUID> {
    Optional<PortfolioEntity> findByUserIdAndName(UUID userId, String name);

    List<Portfolio> findByUserId(UUID userId);
}
