package com.atlas.bank.account.controller;

import com.atlas.bank.account.dto.AccountMapper;
import com.atlas.bank.account.dto.AccountResponse;
import com.atlas.bank.account.dto.CreateAccountRequest;
import com.atlas.bank.account.model.Account;
import com.atlas.bank.account.service.IAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final IAccountService accountService;
    private final AccountMapper accountMapper;

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody CreateAccountRequest accountRequest) {
        Account account = accountMapper.toEntity(accountRequest);
         Account saved = accountService.createAccount(account);

         return ResponseEntity.status(HttpStatus.CREATED).body(accountMapper.toResponse(saved));
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> findAll() {
        List<AccountResponse> accounts = accountService.findAll()
                .stream()
                .map(accountMapper::toResponse)
                .toList();

        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> findById(@PathVariable Long id) {
        Account account = accountService.findById(id);

        return ResponseEntity.ok(accountMapper.toResponse(account));
    }
}
