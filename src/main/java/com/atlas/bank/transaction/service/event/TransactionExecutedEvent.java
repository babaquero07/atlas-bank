package com.atlas.bank.transaction.service.event;

import java.math.BigDecimal;

public record TransactionExecutedEvent(
        Long transactionId,
        String type,
        Long sourceAccountId,
        Long TargetAccountId,
        BigDecimal amount,
        BigDecimal fee
) { }
