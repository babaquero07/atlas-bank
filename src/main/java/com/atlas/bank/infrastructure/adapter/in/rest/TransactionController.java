package com.atlas.bank.infrastructure.adapter.in.rest;

import com.atlas.bank.application.command.TransferMoneyCommand;
import com.atlas.bank.application.port.in.GetTransactionsByAccountUseCase;
import com.atlas.bank.application.port.in.TransferMoneyUseCase;
import com.atlas.bank.application.query.GetAccountStatementQuery;
import com.atlas.bank.application.query.TransactionReadModel;
import com.atlas.bank.infrastructure.adapter.in.rest.dto.TransactionMapper;
import com.atlas.bank.infrastructure.adapter.in.rest.dto.TransactionResponse;
import com.atlas.bank.infrastructure.adapter.in.rest.dto.TransferRequest;
import com.atlas.bank.domain.model.transaction.Transaction;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransferMoneyUseCase transferMoneyUseCase;
    private final GetTransactionsByAccountUseCase getTransactionsByAccountUseCase;
    private final TransactionMapper transactionMapper;

    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponse> transfer(@Valid @RequestBody TransferRequest transferRequest) {
        TransferMoneyCommand command = TransferMoneyCommand
                .builder()
                .toId(transferRequest.getToAccountId())
                .fromId(transferRequest.getToAccountId())
                .amount(transferRequest.getAmount())
                .build();

        Transaction transaction = transferMoneyUseCase.transfer(command);

        return ResponseEntity.ok(transactionMapper.toResponse(transaction));
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<TransactionReadModel>> getTransactions(@PathVariable Long id) {
        GetAccountStatementQuery query = new GetAccountStatementQuery(id);

        List<TransactionReadModel> transactions = getTransactionsByAccountUseCase.getByAccountId(query);

        return ResponseEntity.ok(transactions);
    }
}
