package com.atlas.bank.transaction.service;

import com.atlas.bank.transaction.model.Transaction;

import java.math.BigDecimal;

public interface ITransferService {
    Transaction execute(Long fromId, Long toId, BigDecimal amount);
}
