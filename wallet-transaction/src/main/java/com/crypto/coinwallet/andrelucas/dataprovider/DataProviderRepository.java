package com.crypto.coinwallet.andrelucas.dataprovider;

import java.util.List;
import java.util.Optional;

public interface DataProviderRepository<T, ID>{

    void save(T entity);
    List<T> findAll();
    Optional<T> findById(ID id);
    void deleteAll();
}
