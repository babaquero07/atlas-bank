package com.atlas.bank.infrastructure.adapter.out.persistence;

import com.atlas.bank.application.port.out.AccountRepositoryPort;
import com.atlas.bank.domain.model.account.Account;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaAccountRepositoryAdapter implements AccountRepositoryPort {
    private final SpringDataAccountRepository accountRepository;
    private final AccountPersistenceMapper mapper;

    @Override
    public Optional<Account> findById(Long id) {
        return accountRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Account> findAll() {
      return  accountRepository.findAll().stream()
              .map(mapper::toDomain).toList();
    }

    @Override
    public Account save(Account account) {
        account.initDefaults();

        AccountJpaEntity entity = mapper.toJpaEntity(account);
        AccountJpaEntity savedAccount = accountRepository.save(entity);

        return mapper.toDomain(savedAccount);
    }
}
