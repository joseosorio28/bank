package com.devsu.bank.controller.client;

import com.devsu.bank.business.ClientService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class ClientRouter {

    @Value("${api.endpoint.client}")
    private String path;
    private static final String PATH_VARIABLE = "{idNumber}";
    private final ClientService service;

    public ClientRouter(ClientService clientService) {
        this.service = clientService;
    }

    @Bean
    public RouterFunction<ServerResponse> clientRoutes() {
        return RouterFunctions
                .route(GET(path), serverRequest -> service.getClients())
                .andRoute(GET(path + PATH_VARIABLE), service::getClient)
                .andRoute(POST(path), service::createClient)
                .andRoute(PUT(path + PATH_VARIABLE), service::updateClient)
                .andRoute(DELETE(path + PATH_VARIABLE), service::deleteClient)
                ;
    }

}
