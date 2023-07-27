package com.devsu.bank.advisor.exceptions;

public class ServerException extends RuntimeException {

    public ServerException(String message) {
        super("The server is not available at the moment. " + message);
    }
}
