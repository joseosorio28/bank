package com.devsu.bank.controller;

import com.devsu.bank.business.MovementService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Component
public class MovementRouter {

    @Value("${api.endpoint.movement}")
    private String pathMovement;
    @Value("${api.endpoint.report}")
    private String pathReport;
    private static final String PATH_VARIABLE = "{accountNumber}";
    private final MovementService service;

    public MovementRouter(MovementService service) {
        this.service = service;
    }

    @Bean
    public RouterFunction<ServerResponse> movementRoutes() {
        return RouterFunctions
                .route(GET(pathMovement), serverRequest -> service.getMovements())
                .andRoute(GET(pathMovement + PATH_VARIABLE), service::getMovements)
                .andRoute(POST(pathMovement), service::createMovement)
                .andRoute(PUT(pathMovement + PATH_VARIABLE), service::updateMovement)
                .andRoute(DELETE(pathMovement + PATH_VARIABLE), service::deleteMovement)

                ;
    }

}