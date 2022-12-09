package com.crypto.walletmanager.business.portfolio;

import com.crypto.walletmanager.business.BusinessRepository;

import java.util.List;
import java.util.UUID;

public interface PortfolioBusinessRepository extends BusinessRepository<Portfolio, UUID> {
    List<Portfolio> findBy(UUID userId);
}
