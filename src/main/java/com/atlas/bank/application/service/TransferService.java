package com.atlas.bank.application.service;

import com.atlas.bank.application.command.TransferMoneyCommand;
import com.atlas.bank.application.port.out.TransactionRepositoryPort;
import com.atlas.bank.domain.exception.AccountNotFoundException;
import com.atlas.bank.domain.model.account.Account;
import com.atlas.bank.application.port.in.TransferMoneyUseCase;
import com.atlas.bank.application.port.out.AccountRepositoryPort;
import com.atlas.bank.domain.model.transaction.TransferContext;
import com.atlas.bank.domain.model.transaction.Transaction;
import com.atlas.bank.domain.service.TransferDomainService;
import com.atlas.bank.domain.strategy.fee.FeeCalculator;
import com.atlas.bank.domain.validation.TransferValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransferService extends TransactionProcessor<TransferContext>
        implements TransferMoneyUseCase {

    private final AccountRepositoryPort accountRepository;
    private final List<FeeCalculator> feeCalculators; // To use all implementations of FeeCalculator
    private final List<TransferValidator> transferValidators;
    private final TransferDomainService transferDomainService;

    public TransferService(
            TransactionRepositoryPort transactionRepository,
            AccountRepositoryPort accountRepository,
            List<FeeCalculator> feeCalculators,
            List<TransferValidator> transferValidators,
            TransferDomainService transferDomainService) {
        super(transactionRepository);
        this.accountRepository = accountRepository;
        this.feeCalculators = feeCalculators;
        this.transferValidators = transferValidators;
        this.transferDomainService = transferDomainService;
    }

    @Override
    @Transactional
    public Transaction transfer(TransferMoneyCommand command) {
        Account from = accountRepository.findById(command.fromId())
                .orElseThrow(() -> new AccountNotFoundException(command.fromId()));
        Account to = accountRepository.findById(command.toId())
                .orElseThrow(() -> new AccountNotFoundException(command.toId()));

       Transaction transaction = process(new TransferContext(from, to, command.amount()));

       transaction.executeTransfer();
       transactionRepository.save(transaction);

       return transaction;
    }

    @Override
    protected void validate(TransferContext ctx) {
        transferValidators.forEach(v -> v.validate(ctx));
    }

    @Override
    protected BigDecimal calculateFee(TransferContext ctx) {
      return feeCalculators.stream()
                .filter(fc -> fc.supports(ctx.from().getType()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No hay calculador para el tipo de fee"))
                .calculate(ctx.amount());
    }

    @Override
    protected void execute(TransferContext ctx, BigDecimal fee) {
        transferDomainService.transfer(ctx.from(), ctx.to(), ctx.amount(), fee);

        accountRepository.save(ctx.from());
        accountRepository.save(ctx.to());
    }

    @Override
    protected Transaction save(TransferContext ctx, BigDecimal fee) {
        Transaction transaction = TransactionFactory.createTransfer(ctx, fee);

        return transactionRepository.save(transaction);
    }
}
