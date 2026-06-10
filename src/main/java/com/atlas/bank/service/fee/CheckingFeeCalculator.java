package com.atlas.bank.service.fee;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CheckingFeeCalculator implements FeeCalculator {
    @Override
    public boolean supports(String accountType) {
        return accountType.equalsIgnoreCase("CHECKING");
    }

    @Override
    public BigDecimal calculate(BigDecimal amount) {
        return amount.multiply(new BigDecimal("0.015"));
    }
}
