package com.devsu.bank.advisor.exceptions;

public class AccountFoundException extends RuntimeException {

    public AccountFoundException(String number) {
        super("Account number: " + number + " is already in the database.");
    }
}

