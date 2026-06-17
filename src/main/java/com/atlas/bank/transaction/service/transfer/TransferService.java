package com.atlas.bank.transaction.service.transfer;

import com.atlas.bank.account.exception.AccountNotFoundException;
import com.atlas.bank.account.model.Account;
import com.atlas.bank.transaction.model.Transaction;
import com.atlas.bank.account.repository.AccountRepository;
import com.atlas.bank.transaction.repository.TransactionRepository;
import com.atlas.bank.transaction.service.domain.TransferDomainService;
import com.atlas.bank.transaction.service.factory.TransactionFactory;
import com.atlas.bank.transaction.service.fee.FeeCalculator;
import com.atlas.bank.transaction.validation.chain.TransferValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransferService extends TransactionProcessor<TransferContext> implements ITransferService {
    private final AccountRepository accountRepository;
    private final List<FeeCalculator> feeCalculators; // To use all implementations of FeeCalculator
    private final List<TransferValidator> transferValidators;
    private final TransferDomainService transferDomainService;

    public TransferService(
            TransactionRepository transactionRepository,
            AccountRepository accountRepository,
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
    public Transaction execute(Long fromId, Long toId, BigDecimal amount) {
        Account from = accountRepository.findById(fromId)
                .orElseThrow(() -> new AccountNotFoundException(fromId));
        Account to = accountRepository.findById(toId)
                .orElseThrow(() -> new AccountNotFoundException(toId));

       Transaction transaction = process(new TransferContext(from, to, amount));

       transaction.advanceTo(transaction.getState().validate());
       transaction.advanceTo(transaction.getState().execute());
       transaction.markAsExecuted();
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
