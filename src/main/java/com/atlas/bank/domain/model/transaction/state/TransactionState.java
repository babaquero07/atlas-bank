package com.atlas.bank.domain.model.transaction.state;

import com.atlas.bank.domain.model.transaction.TransactionStatus;

public sealed interface TransactionState permits PendingState, ValidatedState, ExecutedState,
        RejectedState, ReversedState {
    TransactionStatus status();
    default TransactionState execute() {
        throw new IllegalStateException("Cannot execute transaction in current state" + status());
    }
    default TransactionState validate() {
        throw new IllegalStateException("Cannot validate transaction in current state" + status());
    }
    default TransactionState reject(String reason) {
        throw new IllegalStateException("Cannot reject transaction in current state" + status());
    }
    default TransactionState reverse() {
        throw new IllegalStateException("Cannot reverse transaction in current state" + status());
    }
}
