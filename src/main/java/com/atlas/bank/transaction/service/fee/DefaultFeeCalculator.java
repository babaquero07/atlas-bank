package com.atlas.bank.transaction.service.fee;

import com.atlas.bank.account.model.AccounType;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Order() // This ensures that this class is the last one to be executed
public class DefaultFeeCalculator implements FeeCalculator {
    @Override
    public boolean supports(AccounType accountType) {
        return true;
    }

    @Override
    public BigDecimal calculate(BigDecimal amount) {
        return BigDecimal.ZERO;
    }
}
