package com.devsu.bank.mapper;

import com.devsu.bank.domain.Account;
import com.devsu.bank.domain.Movement;
import com.devsu.bank.dtos.*;
import com.devsu.bank.domain.Client;
import com.devsu.bank.domain.Person;

public class Mapper {

    public Person toPersonEntity(ClientDto clientDto) {
        return Person
                .builder()
                .name(clientDto.getName())
                .gender(clientDto.getGender())
                .age(clientDto.getAge())
                .identificationNumber(clientDto.getIdentificationNumber())
                .address(clientDto.getAddress())
                .phoneNumber(clientDto.getPhoneNumber())
                .build();
    }

    public Person toPersonEntity(Client client) {
        return Person
                .builder()
                .id(client.getClientId())
                .name(client.getName())
                .gender(client.getGender())
                .age(client.getAge())
                .identificationNumber(client.getIdentificationNumber())
                .address(client.getAddress())
                .phoneNumber(client.getPhoneNumber())
                .build();
    }

    public Client toClientEntity(ClientDto clientDto) {
        return Client
                .builder()
                .name(clientDto.getName())
                .gender(clientDto.getGender())
                .age(clientDto.getAge())
                .identificationNumber(clientDto.getIdentificationNumber())
                .address(clientDto.getAddress())
                .phoneNumber(clientDto.getPhoneNumber())
                .password(clientDto.getPassword())
                .status(clientDto.isStatus())
                .build();
    }

    public ClientDto toClientDto(Client client) {
        return ClientDto
                .builder()
                .name(client.getName())
                .gender(client.getGender())
                .age(client.getAge())
                .identificationNumber(client.getIdentificationNumber())
                .address(client.getAddress())
                .phoneNumber(client.getPhoneNumber())
                .password(client.getPassword())
                .status(client.isStatus())
                .build();
    }

    public Account toAccountEntity(AccountDetailedDto accountDto) {
        return Account
                .builder()
                .number(accountDto.getNumber())
                .type(accountDto.getType())
                .initialBalance(accountDto.getInitialBalance())
                .status(accountDto.isStatus())
                .build();
    }

    public AccountDto toAccountDto(Account account) {
        return AccountDto
                .builder()
                .number(account.getNumber())
                .type(account.getType())
                .initialBalance(account.getInitialBalance())
                .status(account.isStatus())
                .build();
    }

    public Movement toMovementEntity(MovementDto movementDto) {
        return Movement
                .builder()
                .type(movementDto.getType())
                .amount(movementDto.getAmount())
                .build();
    }

    public MovementDto toMovementDto(Movement movement) {
        return MovementDto
                .builder()
                .type(movement.getType())
                .amount(movement.getAmount())
                .build();
    }

    public MovementDetailedDto toMovementDetailedDto(Movement movement) {
        return MovementDetailedDto
                .builder()
                .transactionDate(movement.getTransactionDate())
                .type(movement.getType())
                .amount(movement.getAmount())
                .balance(movement.getBalance())
                .build();
    }

}
