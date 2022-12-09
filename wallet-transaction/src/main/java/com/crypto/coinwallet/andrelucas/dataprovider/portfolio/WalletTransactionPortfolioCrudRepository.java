package com.crypto.coinwallet.andrelucas.dataprovider.portfolio;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface WalletTransactionPortfolioCrudRepository extends CrudRepository<PortfolioEntity, UUID> {
}
