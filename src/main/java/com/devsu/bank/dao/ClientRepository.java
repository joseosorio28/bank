package com.devsu.bank.dao;

import com.devsu.bank.domain.Client;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ClientRepository extends ReactiveCrudRepository<Client, Integer> {

    @Query("SELECT * FROM client JOIN person ON client.client_id = person.id WHERE identification_number = $1")
    Mono<Client> findByIdNumber(String idNumber);

    @NotNull
    @Query("SELECT * FROM client JOIN person ON client.client_id = person.id")
    Flux<Client> findAll();

    @Query("INSERT INTO client (client_id, password, status) VALUES ($1, $2, $3)")
    Mono<Client> save(Integer clientId, String password, boolean status);

    @Query("UPDATE client SET password=$2, status=$3 WHERE client_id=$1")
    Mono<Client> update(Integer clientId, String password, boolean status);

    Mono<Void> deleteByClientId(Integer clientId);

}
