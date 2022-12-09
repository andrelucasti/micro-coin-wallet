package com.crypto.walletmanager.dataprovider.portfolio;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface PortfolioCrudRepository extends CrudRepository<PortfolioEntity, UUID> {
}
