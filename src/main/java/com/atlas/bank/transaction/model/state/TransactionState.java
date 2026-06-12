package com.atlas.bank.transaction.model.state;

import com.atlas.bank.transaction.model.TransactionStatus;

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
