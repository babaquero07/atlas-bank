package com.atlas.bank.transaction.controller;

import com.atlas.bank.transaction.dto.TransactionMapper;
import com.atlas.bank.transaction.dto.TransactionResponse;
import com.atlas.bank.transaction.dto.TransferRequest;
import com.atlas.bank.transaction.model.Transaction;
import com.atlas.bank.transaction.service.ITransactionQueryService;
import com.atlas.bank.transaction.service.ITransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final ITransferService transferService;
    private final ITransactionQueryService transactionQueryService;
    private final TransactionMapper transactionMapper;

    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponse> transfer(@Valid @RequestBody TransferRequest transferRequest) {
        Transaction transaction = transferService.execute(
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
