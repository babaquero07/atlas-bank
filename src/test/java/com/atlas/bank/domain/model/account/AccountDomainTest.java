package com.atlas.bank.domain.model.account;

import com.atlas.bank.domain.exception.InsufficientFundsException;
import com.atlas.bank.domain.model.shared.Currency;
import com.atlas.bank.domain.model.shared.Email;
import com.atlas.bank.domain.model.shared.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AccountDomainTest {
    private Account createAccountWithBalance(BigDecimal balance) {
        return Account.builder()
                .id(1L)
                .accountNumber("ATL-0001-0001-0001")
                .ownerName("Alexander Baquero")
                .email(Email.of("abaquero@gmail.com"))
                .type(AccountType.SAVINGS)
                .balance(Money.of(balance, Currency.USD))
                .status(AccountStatus.ACTIVE)
                .build();
    }

    @Test
    @DisplayName("Debe depositar dinero correctamente")
    void shouldDepositMoney() {
        Account account = createAccountWithBalance(new BigDecimal(1000));
        Money deposit = Money.of(new BigDecimal(500), Currency.USD);

        account.deposit(deposit);

        assertEquals(Money.of(new BigDecimal(1500), Currency.USD), account.getBalance());
    }

    @Test
    @DisplayName("Retirar dinero con fondos suficientes")
    void shouldWithdrawWhenSufficientFunds() {
        Account account = createAccountWithBalance(new BigDecimal(1000));
        Money withdrawal = Money.of(new BigDecimal(300), Currency.USD);

        account.withdraw(withdrawal);

        assertEquals(Money.of(new BigDecimal("700.00"), Currency.USD), account.getBalance());
    }

    @Test
    @DisplayName("Lanzar exepcion con fondos insuficientes")
    void shouldRejectWithdrawWhenInsufficientFunds() {
        Account account = createAccountWithBalance(new BigDecimal(1000));
        Money withdrawal = Money.of(new BigDecimal(1500), Currency.USD);

        assertThrows(InsufficientFundsException.class, () -> account.withdraw(withdrawal));
    }

    @Test
    @DisplayName("Reject deposit if the amount is negative")
    void shouldRejectDepositIfAmountIsNegative() {
        Account account = createAccountWithBalance(new BigDecimal(1000));
        Money deposit = Money.of(new BigDecimal(-100), Currency.USD);

        assertThrows(IllegalArgumentException.class, () -> account.deposit(deposit));
    }

    @Test
    @DisplayName("Debe inicializar valores por defecto")
    void shouldInitializeAccountWithBalance() {
        Account account = Account
                .builder()
                .accountNumber("ATL-0001-0001-0001")
                .ownerName("Alexander Baquero")
                .email(Email.of("abaquero@gmail.com"))
                .type(AccountType.SAVINGS)
                .build();

        account.initDefaults();

        assertEquals(AccountStatus.ACTIVE, account.getStatus());
        assertEquals(Money.zero(Currency.ARS), account.getBalance());
        assertNotNull(account.getCreatedAt());
    }
}