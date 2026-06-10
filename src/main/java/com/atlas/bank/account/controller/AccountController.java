package com.atlas.bank.account.controller;

import com.atlas.bank.account.model.Account;
import com.atlas.bank.account.service.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final IAccountService accountService;

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
}
