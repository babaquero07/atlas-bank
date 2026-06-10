package com.atlas.bank.transaction.exception;

public class AccountNotActiveException extends RuntimeException {
    public AccountNotActiveException(Long accountId, String status) {
        super("The account with id: " + accountId + " is not active. Status: " + status + "");
    }
}
