package com.devsu.bank.business;

import com.devsu.bank.advisor.ErrorResponse;
import com.devsu.bank.advisor.exceptions.AccountNotFoundException;
import com.devsu.bank.advisor.exceptions.WithdrawalsLimitException;
import com.devsu.bank.dao.AccountRepository;
import com.devsu.bank.dao.MovementRepository;
import com.devsu.bank.domain.Account;
import com.devsu.bank.domain.Movement;
import com.devsu.bank.dtos.MovementDetailedDto;
import com.devsu.bank.dtos.MovementDto;
import com.devsu.bank.mapper.Mapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovementService {

    private static final String PATH_VARIABLE = "accountNumber";
    private static final double MAX_WITHDRAWAL = 1000.0;
    private final MovementRepository movementRepository;
    private final AccountRepository accountRepository;
    private final Mapper mapper;

    public MovementService(MovementRepository movementRepository, AccountRepository accountRepository, Mapper mapper) {
        this.movementRepository = movementRepository;
        this.accountRepository = accountRepository;
        this.mapper = mapper;
    }

    public Mono<ServerResponse> getMovements() {
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(movementRepository
                        .findAll()
                        .map(mapper::toMovementDto), MovementDto.class);
    }

    public Mono<ServerResponse> getMovements(ServerRequest request) {
        String accountNumber = request.pathVariable(PATH_VARIABLE);
        return accountRepository.findByNumber(accountNumber)
                .flatMap(account -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(movementRepository
                                .findAllByAccountId(account.getId())
                                .map(mapper::toMovementDetailedDto), MovementDetailedDto.class))
                .switchIfEmpty(ServerResponse
                        .status(HttpStatus.NOT_FOUND)
                        .bodyValue(new ErrorResponse(new AccountNotFoundException(accountNumber), HttpStatus.NOT_FOUND)))
                ;
    }

    @Transactional
    public Mono<ServerResponse> createMovement(ServerRequest request) {
        return request.bodyToMono(MovementDto.class)
                .flatMap(this::checkBalanceAndSave)
                ;
    }

    private Mono<ServerResponse> checkBalanceAndSave(MovementDto movementDto) {
        return accountRepository
                .findByNumber(movementDto.getNumber())
                .flatMap(account -> checkBalance(account, movementDto))
                .switchIfEmpty(ServerResponse
                        .status(HttpStatus.NOT_FOUND)
                        .bodyValue(new ErrorResponse(new AccountNotFoundException(movementDto.getNumber()), HttpStatus.NOT_FOUND)))
                ;
    }

    private Mono<ServerResponse> checkBalance(Account account, MovementDto movementDto) {
        return movementRepository
                .findAllByAccountId(account.getId())
                .collectList()
                .flatMap(movements -> {
                    double currentBalance = account.getInitialBalance();
                    double amount = movementDto.getAmount();
                    if (!movements.isEmpty()) {
                        Movement latestMovement = movements.get(0);
                        currentBalance = latestMovement.getBalance();
                    }
                    if (!movements.isEmpty() && amount < 0) {
                        LocalDateTime today = LocalDateTime.now();
                        List<Movement> todayMovements = movements.stream()
                                .filter(movement -> movement.getTransactionDate().isEqual(today))
                                .collect(Collectors.toList());
                        double todayTotalWithdrawals = todayMovements.stream()
                                .filter(movement -> movement.getAmount() < 0)
                                .mapToDouble(Movement::getAmount)
                                .sum();
                        if (Math.abs(todayTotalWithdrawals + amount) > MAX_WITHDRAWAL) {
                            return ServerResponse
                                    .status(HttpStatus.CONFLICT)
                                    .bodyValue(new ErrorResponse(new WithdrawalsLimitException(), HttpStatus.CONFLICT));
                        }
                    }
                    return saveMovement(account, movementDto, currentBalance + amount);
                });
    }

    private Mono<ServerResponse> saveMovement(Account account, MovementDto movementDto, double balance) {
        Movement movement = mapper.toMovementEntity(movementDto);
        movement.setAccountId(account.getId());
        movement.setBalance(balance);
        movement.setTransactionDate(LocalDateTime.now());
        return movementRepository.save(movement)
                .flatMap(savedMovement -> ServerResponse
                        .created(URI.create("/movements/" + movementDto.getNumber()))
                        .bodyValue(movementDto));
    }

    public Mono<ServerResponse> deleteMovement(ServerRequest request) {
        return null;
    }

    public Mono<ServerResponse> updateMovement(ServerRequest request) {
        return null;
    }

}
