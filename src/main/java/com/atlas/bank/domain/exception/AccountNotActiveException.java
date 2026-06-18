package com.atlas.bank.domain.exception;

public class AccountNotActiveException extends RuntimeException {
    public AccountNotActiveException(Long accountId, String status) {
        super("The account with id: " + accountId + " is not active. Status: " + status + "");
    }
}
