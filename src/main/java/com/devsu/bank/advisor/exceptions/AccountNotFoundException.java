package com.devsu.bank.advisor.exceptions;

public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(String number) {
        super("Could not find account : " + number);
    }
}

