package com.crypto.coinwallet.andrelucas.business;

import com.crypto.coinwallet.andrelucas.business.portfolio.Portfolio;
import com.google.common.annotations.VisibleForTesting;

import java.util.List;
import java.util.Optional;

public interface BusinessRepository<T, ID> {
    void save(T model);
    @VisibleForTesting
    void deleteAll();
    void deleteById(ID id);
    List<T> findAll();
    Optional<Portfolio> findById(ID id);
}
