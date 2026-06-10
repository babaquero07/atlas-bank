package com.atlas.bank.transaction.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferRequest {
    @NotNull(message = "Transaction type is required")
    private Long fromAccountId;

    @NotNull(message = "Transaction type is required")
    private Long toAccountId;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be a positive number")
    private BigDecimal amount;
}
