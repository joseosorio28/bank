package com.devsu.bank.business;

import com.devsu.bank.advisor.ErrorResponse;
import com.devsu.bank.advisor.exceptions.AccountFoundException;
import com.devsu.bank.advisor.exceptions.AccountNotFoundException;
import com.devsu.bank.advisor.exceptions.PersonNotFoundException;
import com.devsu.bank.dao.AccountRepository;
import com.devsu.bank.dao.ClientRepository;
import com.devsu.bank.domain.Account;
import com.devsu.bank.domain.Client;
import com.devsu.bank.dtos.AccountDetailedDto;
import com.devsu.bank.dtos.AccountDto;
import com.devsu.bank.mapper.Mapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Service
public class AccountService {

    private static final String PATH_VARIABLE = "accountNumber";
    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;
    private final Mapper mapper;

    public AccountService(AccountRepository accountRepository, ClientRepository clientRepository, Mapper mapper) {
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
        this.mapper = mapper;
    }

    public Mono<ServerResponse> getAccounts() {
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(accountRepository
                        .findAll()
                        .map(mapper::toAccountDto), AccountDto.class);
    }

    public Mono<ServerResponse> getAccount(ServerRequest request) {
        String accountNumber = request.pathVariable(PATH_VARIABLE);
        return accountRepository.findByNumber(accountNumber)
                .flatMap(account -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(mapper.toAccountDto(account)))
                .switchIfEmpty(ServerResponse
                        .status(HttpStatus.NOT_FOUND)
                        .bodyValue(new ErrorResponse(new AccountNotFoundException(accountNumber), HttpStatus.NOT_FOUND)))
                ;
    }

    @Transactional
    public Mono<ServerResponse> createAccount(ServerRequest request) {
        return request.bodyToMono(AccountDetailedDto.class)
                .flatMap(this::checkIfExistAndSave)
                ;
    }

    private Mono<ServerResponse> checkIfExistAndSave(AccountDetailedDto accountDto) {
        return clientRepository
                .findByIdNumber(accountDto.getIdentificationNumber())
                .flatMap(client -> Mono.defer(() ->
                        accountRepository
                                .findByNumber(accountDto.getNumber())
                                .flatMap(account -> ServerResponse
                                        .status(HttpStatus.CONFLICT)
                                        .bodyValue(new ErrorResponse(
                                                new AccountFoundException(account.getNumber()), HttpStatus.CONFLICT)))
                                .switchIfEmpty(saveAccount(client, accountDto))
                ))
                .switchIfEmpty(ServerResponse
                        .status(HttpStatus.NOT_FOUND)
                        .bodyValue(new ErrorResponse(new PersonNotFoundException(accountDto.getIdentificationNumber()), HttpStatus.NOT_FOUND)))
                ;
    }

    private Mono<ServerResponse> saveAccount(Client client, AccountDetailedDto accountDto) {
        Account account = mapper.toAccountEntity(accountDto);
        account.setClientId(client.getId());
        return accountRepository.save(account)
                .flatMap(savedAccount -> ServerResponse
                        .created(URI.create("/accounts/" + savedAccount.getNumber()))
                        .bodyValue(accountDto));
    }

    @Transactional
    public Mono<ServerResponse> deleteAccount(ServerRequest request) {
        String accountNumber = request.pathVariable(PATH_VARIABLE);
        return accountRepository
                .findByNumber(accountNumber)
                .flatMap(this::delete)
                .switchIfEmpty(ServerResponse
                        .status(HttpStatus.NOT_FOUND)
                        .bodyValue(new ErrorResponse(new AccountNotFoundException(accountNumber), HttpStatus.NOT_FOUND)));
    }

    private Mono<ServerResponse> delete(Account account) {
        return accountRepository
                .deleteById(account.getId())
                .then(ServerResponse
                        .ok()
                        .bodyValue("Account number " + account.getNumber() + " was deleted successfully"));
    }

    @Transactional
    public Mono<ServerResponse> updateAccount(ServerRequest request) {
        String accountNumber = request.pathVariable(PATH_VARIABLE);
        return accountRepository
                .findByNumber(accountNumber)
                .flatMap(account -> Mono.defer(() -> checkUpdate(account, request.bodyToMono(AccountDto.class))))
                .switchIfEmpty(ServerResponse
                        .status(HttpStatus.NOT_FOUND)
                        .bodyValue(new ErrorResponse(new AccountNotFoundException(accountNumber), HttpStatus.NOT_FOUND)))
                ;
    }

    private Mono<ServerResponse> checkUpdate(Account account, Mono<AccountDto> accountDtoMono) {
        return accountDtoMono
                .flatMap(accountDto -> accountRepository
                        .findByNumber(accountDto.getNumber())
                        .flatMap(foundAccount -> ServerResponse
                                .status(HttpStatus.CONFLICT)
                                .bodyValue(new ErrorResponse(new AccountFoundException(foundAccount.getNumber()), HttpStatus.CONFLICT))
                        )
                        .switchIfEmpty(update(account, accountDto))
                );
    }

    private Mono<ServerResponse> update(Account account, AccountDto accountDto) {
        account.setNumber(accountDto.getNumber());
        account.setType(accountDto.getType());
        account.setInitialBalance(accountDto.getInitialBalance());
        account.setStatus(accountDto.isStatus());
        return accountRepository
                .save(account)
                .then(ServerResponse
                        .accepted()
                        .bodyValue(accountDto));
    }

}
