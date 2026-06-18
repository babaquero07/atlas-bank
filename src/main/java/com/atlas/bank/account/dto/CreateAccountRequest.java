package com.atlas.bank.account.dto;

import com.atlas.bank.domain.model.account.AccountType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateAccountRequest {
    @NotBlank(message = "Account number is required")
    private String accountNumber;

    @NotBlank(message = "Owner name is required")
    private String ownerName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "Account type is required")
    private AccountType type;

    @PositiveOrZero(message = "Balance must be a positive number")
    private BigDecimal balance;
}
