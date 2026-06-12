package com.atlas.bank.transaction.service.fraud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Slf4j
public class ExternalFraudCheckAdapter implements FraudChecker {
    @Override
    public FraudCheckResult check(Long accountId, BigDecimal amount) {
        ExternalFraudResponse res = callExternalApi(accountId, amount);

        log.info("Respuesta del servicio de fraude {}", res);

        if("BLOCK".equals(res.getRecommendation())) {
            return FraudCheckResult.blocked("Operación bloqueada por fraude" + res.getRiskLevel() +
                    " - " + res.getRecommendation());
        }

        return FraudCheckResult.allowed();
    }

    private ExternalFraudResponse callExternalApi(Long accountId, BigDecimal amount) {
        if(amount.compareTo(new BigDecimal("1000000")) > 0) {
            return new ExternalFraudResponse("HIGH", 0.95, "BLOCK");
        }

        return new ExternalFraudResponse("LOW", 0.1, "ALLOW");
    }
}
