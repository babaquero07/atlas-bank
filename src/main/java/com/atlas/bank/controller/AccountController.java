package com.atlas.bank.controller;

import com.atlas.bank.model.Account;
import com.atlas.bank.model.Transaction;
import com.atlas.bank.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final IAccountService accountService;
    private final ITransferService transferService;
    private final ITransactionQueryService transactionQueryService;

    @PostMapping
    public ResponseEntity<Account> createAccount(Account account) {
        Account createdAccount = accountService.createAccount(account);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
    }

    @GetMapping
    public ResponseEntity<List<Account>> findAll() {
        List<Account> accounts = accountService.findAll();

        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> findById(Long id) {
        Account account = accountService.findById(id);

        return ResponseEntity.ok(account);
    }

    @PostMapping("/transfer")
    public ResponseEntity<Transaction> transfer(@RequestParam Long fromId,
                                                @RequestParam Long toId,
                                                @RequestParam BigDecimal amount) {
        Transaction transaction = transferService.execute(fromId, toId, amount);


        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/{id}/transactions")
    public ResponseEntity<List<Transaction>> getTransactions(@PathVariable Long id) {
        List<Transaction> transactions = transactionQueryService.getByAccountId(id);

        return ResponseEntity.ok(transactions);
    }
}
