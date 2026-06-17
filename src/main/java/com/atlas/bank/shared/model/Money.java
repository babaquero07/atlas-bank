package com.atlas.bank.shared.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@Embeddable // Says to JPA doesn't create a table for this class.
@NoArgsConstructor
@EqualsAndHashCode
public class Money {
    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 3)
    private Currency currency;

    // private constructor to avoid creating Money objects directly
    private Money(BigDecimal amount, Currency currency) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }

        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
        this.currency = currency;
    }

    public static Money of(BigDecimal amount, Currency currency) {
        return new Money(amount, currency);
    }

    public static Money zero(Currency currency) {
        return new Money(BigDecimal.ZERO, currency);
    }

    private void validateSameCurrency(Money other) {
        if (currency != other.currency) {
            throw new IllegalArgumentException("Currencies must be the same: " + currency + " != " + other.currency);
        }
    }

    public Money add(Money other) {
        validateSameCurrency(other);

        return Money.of(this.amount.add(other.amount), currency);
    }

    public Money subtract(Money other) {
        validateSameCurrency(other);

        return Money.of(this.amount.subtract(other.amount), currency);
    }

    public boolean isGreaterThan(Money other) {
        validateSameCurrency(other);

        return this.amount.compareTo(other.amount) > 0;
    }

    public boolean isLessThan(Money other) {
        validateSameCurrency(other);

        return this.amount.compareTo(other.amount) < 0;
    }

    public boolean isNegative() {
        return this.amount.compareTo(BigDecimal.ZERO) < 0;
    }

    @Override
    public String toString() {
        // 150.00 USD
        return amount.toPlainString() + " " + currency;
    }
}
