package com.devsu.bank.dao;

import com.devsu.bank.domain.Movement;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface MovementRepository extends ReactiveCrudRepository<Movement, Integer> {

    Flux<Movement> findAllByAccountId(Integer accountId);

}
