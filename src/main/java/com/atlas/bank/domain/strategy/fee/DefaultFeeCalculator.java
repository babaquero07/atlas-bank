package com.atlas.bank.domain.strategy.fee;

import com.atlas.bank.domain.model.account.AccountType;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Order() // This ensures that this class is the last one to be executed
public class DefaultFeeCalculator implements FeeCalculator {
    @Override
    public boolean supports(AccountType accountType) {
        return true;
    }

    @Override
    public BigDecimal calculate(BigDecimal amount) {
        return BigDecimal.ZERO;
    }
}
