package com.devsu.bank.advisor.exceptions;

public class WithdrawalsLimitException extends RuntimeException {

    public WithdrawalsLimitException() {
        super("Daily withdrawal quota exceeded");
    }
}

