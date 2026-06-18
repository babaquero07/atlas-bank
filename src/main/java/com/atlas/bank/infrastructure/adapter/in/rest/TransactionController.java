package com.atlas.bank.infrastructure.adapter.in.rest;

import com.atlas.bank.application.port.in.TransferMoneyUseCase;
import com.atlas.bank.infrastructure.adapter.in.rest.dto.TransactionMapper;
import com.atlas.bank.infrastructure.adapter.in.rest.dto.TransactionResponse;
import com.atlas.bank.infrastructure.adapter.in.rest.dto.TransferRequest;
import com.atlas.bank.domain.model.transaction.Transaction;
import com.atlas.bank.application.service.ITransactionQueryService;
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
    private final ITransactionQueryService transactionQueryService;
    private final TransactionMapper transactionMapper;

    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponse> transfer(@Valid @RequestBody TransferRequest transferRequest) {
        Transaction transaction = transferMoneyUseCase.execute(
                transferRequest.getFromAccountId(),
                transferRequest.getToAccountId(),
                transferRequest.getAmount()
        );

        return ResponseEntity.ok(transactionMapper.toResponse(transaction));
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<TransactionResponse>> getTransactions(@PathVariable Long id) {
        List<TransactionResponse> transactions = transactionQueryService.getByAccountId(id)
                .stream()
                .map(transactionMapper::toResponse)
                .toList();

        return ResponseEntity.ok(transactions);
    }
}
