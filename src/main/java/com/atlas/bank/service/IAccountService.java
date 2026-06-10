package com.atlas.bank.service;

import com.atlas.bank.model.Account;

import java.util.List;

public interface IAccountService {
    Account createAccount(Account account);
    List<Account> findAll();
    Account findById(Long id);
}
