package com.atlas.bank.transaction.service.fee;

import com.atlas.bank.account.model.AccounType;

import java.math.BigDecimal;

public interface FeeCalculator {
    boolean supports(AccounType accountType);
    BigDecimal calculate(BigDecimal amount);
}
