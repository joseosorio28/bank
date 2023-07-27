package com.devsu.bank.advisor.exceptions;

public class RequestException extends RuntimeException {
    public RequestException() {
        super("Client is making to many consecutive request.");
    }
}
