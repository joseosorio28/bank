package com.devsu.bank.controller;

import com.devsu.bank.business.AccountService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class AccountRouter {

    @Value("${api.endpoint.account}")
    private String path;
    private static final String PATH_VARIABLE = "{accountNumber}";
    private final AccountService service;

    public AccountRouter(AccountService service) {
        this.service = service;
    }

    @Bean
    public RouterFunction<ServerResponse> accountRoutes() {
        return RouterFunctions
                .route(GET(path), serverRequest -> service.getAccounts())
                .andRoute(GET(path + PATH_VARIABLE), service::getAccount)
                .andRoute(POST(path), service::createAccount)
                .andRoute(PUT(path + PATH_VARIABLE), service::updateAccount)
                .andRoute(DELETE(path + PATH_VARIABLE), service::deleteAccount)
                ;
    }
}