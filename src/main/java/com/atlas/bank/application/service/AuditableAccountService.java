package com.atlas.bank.application.service;

import com.atlas.bank.application.command.CreateAccountCommand;
import com.atlas.bank.application.port.in.CreateAccountUseCase;
import com.atlas.bank.application.port.in.GetAccountUseCase;
import com.atlas.bank.application.port.in.ListAccountsUseCase;
import com.atlas.bank.domain.model.account.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@Primary // This annotation is used to mark the primary bean
public class AuditableAccountService implements CreateAccountUseCase, ListAccountsUseCase, GetAccountUseCase {
    private final CreateAccountUseCase createAccountUseCase;
    private final ListAccountsUseCase listAccountsUseCase;
    private final GetAccountUseCase getAccountUseCase;

    public AuditableAccountService(
            CreateAccountUseCase createAccountUseCase,
            ListAccountsUseCase listAccountsUseCase,
            GetAccountUseCase getAccountUseCase) {
        this.createAccountUseCase = createAccountUseCase;
        this.listAccountsUseCase = listAccountsUseCase;
        this.getAccountUseCase = getAccountUseCase;
    }

    @Override
    public Account create(CreateAccountCommand command) {
        log.info("AuditableAccountService: Creating account {}", command);

        Account created = createAccountUseCase.create(command);
        log.info("AuditableAccountService: Created account {}", created.getId());

        return created;
    }

    @Override
    public List<Account> findAll() {
        log.info("AuditableAccountService: Finding all accounts");

        return listAccountsUseCase.findAll();
    }

    @Override
    public Account findById(Long id) {
        log.info("AuditableAccountService: Finding account by id {}", id);

        return getAccountUseCase.findById(id);
    }
}
