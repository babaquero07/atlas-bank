package com.atlas.bank.infrastructure.adapter.in.rest;

import com.atlas.bank.application.port.in.CreateAccountUseCase;
import com.atlas.bank.application.port.in.GetAccountUseCase;
import com.atlas.bank.application.port.in.ListAccountsUseCase;
import com.atlas.bank.infrastructure.adapter.in.rest.dto.AccountMapper;
import com.atlas.bank.infrastructure.adapter.in.rest.dto.AccountResponse;
import com.atlas.bank.infrastructure.adapter.in.rest.dto.CreateAccountRequest;
import com.atlas.bank.infrastructure.adapter.in.rest.dto.DashboardResponse;
import com.atlas.bank.domain.model.account.Account;
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
    private final CreateAccountUseCase createAccountUseCase;
    private final ListAccountsUseCase listAccountsUseCase;
    private final GetAccountUseCase getAccountUseCase;
    private final AccountMapper accountMapper;
    private final AccountDashboardFacade accountDashboardFacade;

    @GetMapping("/{id}/dashboard")
    public ResponseEntity<DashboardResponse> getDashboard(@PathVariable Long id) {
        return ResponseEntity.ok(accountDashboardFacade.getDashboard(id));
    }

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody CreateAccountRequest accountRequest) {
        Account account = accountMapper.toEntity(accountRequest);
        Account saved = createAccountUseCase.create(account);

        return ResponseEntity.status(HttpStatus.CREATED).body(accountMapper.toResponse(saved));
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> findAll() {
        List<AccountResponse> accounts = listAccountsUseCase.findAll()
                .stream()
                .map(accountMapper::toResponse)
                .toList();

        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> findById(@PathVariable Long id) {
        Account account = getAccountUseCase.findById(id);

        return ResponseEntity.ok(accountMapper.toResponse(account));
    }
}
