package com.devsu.bank.advisor.exceptions;

public class PersonNotFoundException extends RuntimeException {

    public PersonNotFoundException(String id) {
        super("Could not find client with id: " + id);
    }
}

