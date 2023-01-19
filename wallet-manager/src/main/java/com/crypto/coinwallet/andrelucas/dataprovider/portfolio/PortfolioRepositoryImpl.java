package com.crypto.coinwallet.andrelucas.dataprovider.portfolio;

import com.crypto.coinwallet.andrelucas.business.portfolio.Portfolio;
import com.crypto.coinwallet.andrelucas.business.portfolio.PortfolioBusinessRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Repository
public class PortfolioRepositoryImpl implements PortfolioBusinessRepository {

    private final PortfolioConverter portfolioConverter;
    private final PortfolioCrudRepository portfolioCrudRepository;

    public PortfolioRepositoryImpl(final PortfolioConverter portfolioConverter,
                                   final PortfolioCrudRepository portfolioCrudRepository) {
        this.portfolioConverter = portfolioConverter;
        this.portfolioCrudRepository = portfolioCrudRepository;
    }

    @Override
    public void save(Portfolio portfolio) {
        var portfolioEntity = new PortfolioEntity(portfolio.name(), portfolio.userId());
        portfolioCrudRepository.save(portfolioEntity);
    }

    @Override
    public void deleteAll() {
        portfolioCrudRepository.deleteAll();
    }

    @Override
    public void deleteById(UUID id) {
        portfolioCrudRepository.deleteById(id);
    }

    @Override
    public List<Portfolio> findAll() {
        return StreamSupport.stream(portfolioCrudRepository.findAll().spliterator(), false)
                .map(portfolioConverter::convertFromEntityToModel)
                .toList();
    }

    @Override
    public Optional<Portfolio> findById(UUID id) {
        return portfolioCrudRepository.findById(id)
                .map(portfolioConverter::convertFromEntityToModel);

    }

    @Override
    public List<Portfolio> findBy(UUID userId) {
        return portfolioCrudRepository.findByUserId(userId);
    }

    @Override
    public Optional<Portfolio> findByUserIdAndName(UUID userId, String name) {
        return portfolioCrudRepository.findByUserIdAndName(userId, name)
                .map(portfolioConverter::convertFromEntityToModel);
    }
}
