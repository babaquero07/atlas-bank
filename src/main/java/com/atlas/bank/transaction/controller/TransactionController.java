package com.atlas.bank.transaction.controller;

import com.atlas.bank.transaction.model.Transaction;
import com.atlas.bank.transaction.service.ITransactionQueryService;
import com.atlas.bank.transaction.service.ITransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final ITransferService transferService;
    private final ITransactionQueryService transactionQueryService;

    @PostMapping("/transfer")
    public ResponseEntity<Transaction> transfer(@RequestParam Long fromId,
                                                @RequestParam Long toId,
                                                @RequestParam BigDecimal amount) {
        Transaction transaction = transferService.execute(fromId, toId, amount);


        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<Transaction>> getTransactions(@PathVariable Long id) {
        List<Transaction> transactions = transactionQueryService.getByAccountId(id);

        return ResponseEntity.ok(transactions);
    }
}
