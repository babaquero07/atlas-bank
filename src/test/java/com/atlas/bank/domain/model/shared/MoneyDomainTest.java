package com.atlas.bank.domain.model.shared;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class MoneyDomainTest {
    @Test
    @DisplayName("Debe sumar dos montos en la misma moneda")
    void shouldAddSameCurrency() {
        Money a = Money.of(new BigDecimal("100"), Currency.USD);
        Money b = Money.of(new BigDecimal("250.50"), Currency.USD);

        Money result = a.add(b);

        assertEquals(Money.of(new BigDecimal("350.50"), Currency.USD), result);
    }

    @Test
    @DisplayName("Debería rechazar la operación entre monedas distintas")
    void shouldRejectOperationBetweenDifferentCurrencies() {
        Money a = Money.of(new BigDecimal("100"), Currency.USD);
        Money b = Money.of(new BigDecimal("250.50"), Currency.ARS);

        assertThrows(IllegalArgumentException.class, () -> a.add(b));
    }

    @Test
    @DisplayName("Debe detectar monto negativo")
    void shouldDetectNegativeAmount() {
        Money money = Money.of(new BigDecimal("-100"), Currency.USD);

        assertTrue(money.isNegative());
    }

    @Test
    @DisplayName("Debe comparar montos correctamente")
    void shouldCompareAmount() {
        Money hundred = Money.of(new BigDecimal("100"), Currency.USD);
        Money fifty = Money.of(new BigDecimal("50"), Currency.USD);

        assertTrue(hundred.isGreaterThan(fifty));
        assertFalse(hundred.isLessThan(fifty));
    }

    @Test
    @DisplayName("Money.zero debe tener monto cero")
    void shouldZeroAmount() {
        Money money = Money.zero(Currency.USD);

        assertEquals(new BigDecimal("0.00"), money.getAmount());
        assertFalse(money.isNegative());
    }
}