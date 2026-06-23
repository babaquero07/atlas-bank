package com.atlas.bank.infrastructure.adapter.in.rest;

import com.atlas.bank.application.command.CloseAccountCommand;
import com.atlas.bank.application.command.CreateAccountCommand;
import com.atlas.bank.application.facade.AccountDashboardFacade;
import com.atlas.bank.application.port.in.CloseAccountUseCase;
import com.atlas.bank.application.port.in.CreateAccountUseCase;
import com.atlas.bank.application.port.in.GetAccountUseCase;
import com.atlas.bank.application.port.in.ListAccountsUseCase;
import com.atlas.bank.application.query.DashboardReadModel;
import com.atlas.bank.infrastructure.adapter.in.rest.dto.AccountMapper;
import com.atlas.bank.infrastructure.adapter.in.rest.dto.AccountResponse;
import com.atlas.bank.infrastructure.adapter.in.rest.dto.CreateAccountRequest;
import com.atlas.bank.domain.model.account.Account;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final CreateAccountUseCase createAccountUseCase;
    private final ListAccountsUseCase listAccountsUseCase;
    private final GetAccountUseCase getAccountUseCase;
    private final CloseAccountUseCase closeAccountUseCase;
    private final AccountMapper accountMapper;
    private final AccountDashboardFacade accountDashboardFacade;

    @GetMapping("/{id}/dashboard")
    public ResponseEntity<DashboardReadModel> getDashboard(@PathVariable Long id) {
        return ResponseEntity.ok(accountDashboardFacade.getDashboard(id));
    }

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody CreateAccountRequest accountRequest) {
        CreateAccountCommand command = CreateAccountCommand
                .builder()
                .accountNumber(accountRequest.getAccountNumber())
                .ownerName(accountRequest.getOwnerName())
                .email(accountRequest.getEmail())
                .type(accountRequest.getType())
                .balance(accountRequest.getBalance())
                .build();

        Account saved = createAccountUseCase.create(command);

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

    @PatchMapping("/{id}/close")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AccountResponse> closeAccount(@PathVariable Long id) {
        CloseAccountCommand command = new CloseAccountCommand(id);

        Account closed = closeAccountUseCase.close(command);

        return ResponseEntity.ok(accountMapper.toResponse(closed));
    }
}
