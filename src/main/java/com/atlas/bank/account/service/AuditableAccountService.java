package com.atlas.bank.account.service;

import com.atlas.bank.account.model.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@Primary // This annotation is used to mark the primary bean
public class AuditableAccountService implements IAccountService {
    private final IAccountService delegate;

    public AuditableAccountService(@Qualifier("accountService") IAccountService delegate) {
        this.delegate = delegate;
    }

    @Override
    public Account createAccount(Account account) {
        log.info("AuditableAccountService: Creating account {}", account);

        Account created = delegate.createAccount(account);
        log.info("AuditableAccountService: Created account {}", created.getId());

        return created;
    }

    @Override
    public List<Account> findAll() {
        log.info("AuditableAccountService: Finding all accounts");

        return delegate.findAll();
    }

    @Override
    public Account findById(Long id) {
        log.info("AuditableAccountService: Finding account by id {}", id);

        return delegate.findById(id);
    }
}
