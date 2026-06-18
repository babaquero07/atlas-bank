package com.atlas.bank.domain.exception;

import java.math.BigDecimal;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(Long accountId, BigDecimal balance, BigDecimal amount) {
        super("The account with id: " + accountId + " has insufficient funds. Balance: " + balance + ", Amount: " + amount + "");
    }
}
