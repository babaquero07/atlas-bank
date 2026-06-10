package com.atlas.bank.service;

import com.atlas.bank.model.Transaction;
import com.atlas.bank.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionQueryService {
    private final TransactionRepository transactionRepository;

    public List<Transaction> getByAccountId(Long accountId) {
        return transactionRepository
                .findBySourceAccountIdOrTargetAccountId(accountId, accountId);
    }
}
