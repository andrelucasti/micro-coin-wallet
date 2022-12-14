package com.crypto.coinwallet.andrelucas.business;

import com.google.common.annotations.VisibleForTesting;

import java.util.List;
import java.util.Optional;

public interface BusinessRepository<T, ID>{
    void save(T t);
    List<T> findAll();
    Optional<T> findById(ID id);

    @VisibleForTesting
    void deleteAll();
}
