package com.atlas.bank.transaction.service.fee;

import com.atlas.bank.account.model.AccounType;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Order(1)
public class PremiumFeeCalculator implements FeeCalculator {
    @Override
    public boolean supports(AccounType accountType) {
        return accountType == AccounType.PREMIUM;
    }

    @Override
    public BigDecimal calculate(BigDecimal amount) {
        return BigDecimal.ZERO;
    }
}
