package com.devsu.bank.advisor.exceptions;

public class ClientFoundException extends RuntimeException {

    public ClientFoundException(String id) {
        super("Client with identification number: " + id + " is already in the database.");
    }
}

