package com.crypto.coinwallet.andrelucas.dataprovider.portfolio;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface PortfolioCrudRepository extends CrudRepository<PortfolioEntity, UUID> {
    Optional<PortfolioEntity> findByUserIdAndName(UUID userId, String name);
}
