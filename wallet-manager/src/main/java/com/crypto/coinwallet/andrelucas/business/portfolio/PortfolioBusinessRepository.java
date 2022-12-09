package com.crypto.coinwallet.andrelucas.business.portfolio;

import com.crypto.coinwallet.andrelucas.business.BusinessRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PortfolioBusinessRepository extends BusinessRepository<Portfolio, UUID> {
    List<Portfolio> findBy(UUID userId);
    Optional<Portfolio> findByUserIdAndName(UUID userId, String name);
}
