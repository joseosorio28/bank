package com.devsu.bank.mapper;

import com.devsu.bank.dtos.ClientDto;
import com.devsu.bank.entities.Client;
import com.devsu.bank.entities.Person;

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
                .state(client.isState())
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
                .state(clientDto.isState())
                .build();
    }

}
