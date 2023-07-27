package com.devsu.bank.business;

import com.devsu.bank.advisor.ErrorResponse;
import com.devsu.bank.advisor.exceptions.ClientFoundException;
import com.devsu.bank.advisor.exceptions.PersonNotFoundException;
import com.devsu.bank.dao.ClientRepository;
import com.devsu.bank.dao.PersonRepository;
import com.devsu.bank.dtos.ClientDto;
import com.devsu.bank.entities.Client;
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
public class ClientService {

    private static final String PATH_VARIABLE = "idNumber";
    private final ClientRepository clientRepository;
    private final PersonRepository personRepository;
    private final Mapper mapper;

    public ClientService(ClientRepository clientRepository, PersonRepository personRepository, Mapper mapper) {
        this.clientRepository = clientRepository;
        this.personRepository = personRepository;
        this.mapper = mapper;
    }

    public Mono<ServerResponse> getClients() {
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(clientRepository
                        .findAll()
                        .map(mapper::toClientDto), ClientDto.class);
    }

    public Mono<ServerResponse> getClient(ServerRequest request) {
        String idNumber = request.pathVariable(PATH_VARIABLE);
        return clientRepository.findByIdNumber(idNumber)
                .flatMap(client -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(mapper.toClientDto(client)))
                .switchIfEmpty(ServerResponse
                        .status(HttpStatus.NOT_FOUND)
                        .bodyValue(new ErrorResponse(new PersonNotFoundException(idNumber), HttpStatus.NOT_FOUND)))
                ;
    }

    @Transactional
    public Mono<ServerResponse> createClient(ServerRequest request) {
        return request.bodyToMono(ClientDto.class)
                .flatMap(this::checkIfExistAndSave)
                ;
    }

    private Mono<ServerResponse> checkIfExistAndSave(ClientDto clientDto) {
        return clientRepository
                .findByIdNumber(clientDto.getIdentificationNumber())
                .flatMap(client -> ServerResponse
                        .status(HttpStatus.CONFLICT)
                        .bodyValue(new ErrorResponse(
                                new ClientFoundException(client.getIdentificationNumber()), HttpStatus.CONFLICT)))
                .switchIfEmpty(saveClient(clientDto));
    }

    private Mono<ServerResponse> saveClient(ClientDto clientDto) {
        return personRepository.save(mapper.toPersonEntity(clientDto))
                .flatMap(person -> clientRepository
                        .save(person.getId(),
                                clientDto.getPassword(),
                                clientDto.isState())
                        .switchIfEmpty(Mono.just(mapper.toClientEntity(clientDto))))
                .flatMap(client -> ServerResponse
                        .created(URI.create("/clients/" + client.getIdentificationNumber()))
                        .bodyValue(mapper.toClientDto(client)));
    }

    @Transactional
    public Mono<ServerResponse> updateClient(ServerRequest request) {
        String idNumber = request.pathVariable(PATH_VARIABLE);
        return clientRepository.findByIdNumber(idNumber)
                .flatMap(client -> checkUpdate(client, request.bodyToMono(ClientDto.class)))
                .switchIfEmpty(ServerResponse
                        .status(HttpStatus.NOT_FOUND)
                        .bodyValue(new ErrorResponse(new PersonNotFoundException(idNumber), HttpStatus.NOT_FOUND))
                );
    }

    private Mono<ServerResponse> checkUpdate(Client client, Mono<ClientDto> clientDtoMono) {
        return Mono.defer(() -> clientDtoMono)
                .flatMap(clientDto -> clientRepository.findByIdNumber(clientDto.getIdentificationNumber()))
                .flatMap(foundClient -> ServerResponse
                        .status(HttpStatus.CONFLICT)
                        .bodyValue(new ErrorResponse(new ClientFoundException(foundClient.getIdentificationNumber()), HttpStatus.CONFLICT))
                )
                .switchIfEmpty(update(client, clientDtoMono));
    }

    private Mono<ServerResponse> update(Client client, Mono<ClientDto> clientDtoMono) {
        return clientDtoMono
                .map(clientDto -> {
                    client.setName(clientDto.getName());
                    client.setGender(clientDto.getGender());
                    client.setAge(clientDto.getAge());
                    client.setIdentificationNumber(clientDto.getIdentificationNumber());
                    client.setAddress(clientDto.getAddress());
                    client.setPhoneNumber(clientDto.getPhoneNumber());
                    client.setPassword(clientDto.getPassword());
                    client.setState(clientDto.isState());
                    return client;
                })
                .flatMap(personRepository::save)
                .flatMap(clientUpdated -> ServerResponse
                        .accepted()
                        .bodyValue(clientUpdated));
    }

    @Transactional
    public Mono<ServerResponse> deleteClient(ServerRequest request) {
        String idNumber = request.pathVariable(PATH_VARIABLE);
        return clientRepository
                .findByIdNumber(idNumber)
                .flatMap(this::delete)
                .switchIfEmpty(ServerResponse
                        .status(HttpStatus.NOT_FOUND)
                        .bodyValue(new ErrorResponse(new PersonNotFoundException(idNumber), HttpStatus.NOT_FOUND)));
    }

    private Mono<ServerResponse> delete(Client client) {
        return clientRepository
                .deleteByClientId(client.getClientId())
                .then(personRepository.deleteById(client.getId()))
                .then(ServerResponse
                        .ok()
                        .bodyValue("Client with identification " + client.getIdentificationNumber() + " was deleted successfully"));
    }
}