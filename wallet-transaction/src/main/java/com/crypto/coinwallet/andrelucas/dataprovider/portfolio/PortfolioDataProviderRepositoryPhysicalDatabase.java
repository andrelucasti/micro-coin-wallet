package com.crypto.coinwallet.andrelucas.dataprovider.portfolio;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Repository
public class PortfolioDataProviderRepositoryPhysicalDatabase implements PortfolioDataProviderRepository{

    private final WalletTransactionPortfolioCrudRepository walletTransactionPortfolioCrudRepository;

    public PortfolioDataProviderRepositoryPhysicalDatabase(final WalletTransactionPortfolioCrudRepository walletTransactionPortfolioCrudRepository) {
        this.walletTransactionPortfolioCrudRepository = walletTransactionPortfolioCrudRepository;
    }

    @Override
    public void save(PortfolioEntity entity) {
        walletTransactionPortfolioCrudRepository.save(entity);
    }

    @Override
    public List<PortfolioEntity> findAll() {
        return StreamSupport.stream(walletTransactionPortfolioCrudRepository.findAll().spliterator(), false).toList();
    }

    @Override
    public Optional<PortfolioEntity> findById(UUID id) {
        return walletTransactionPortfolioCrudRepository.findById(id);
    }

    @Override
    public void deleteAll() {
        walletTransactionPortfolioCrudRepository.deleteAll();
    }
}
