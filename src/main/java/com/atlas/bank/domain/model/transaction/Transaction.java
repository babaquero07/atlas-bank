package com.atlas.bank.domain.model.transaction;

import com.atlas.bank.domain.model.transaction.state.*;
import com.atlas.bank.domain.event.TransactionExecutedEvent;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Transaction {
    @EqualsAndHashCode.Include
    private Long id;
    private TransactionType type;
    private Long sourceAccountId;
    private Long targetAccountId;
    private BigDecimal amount;
    private BigDecimal fee;
    private TransactionStatus status;
    private LocalDateTime createdAt;
    private TransactionState state;

    @Builder.Default
    private final List<Object> domainEvents = new ArrayList<>();

    public void initDefaults() {
        if(this.createdAt == null) this.createdAt = LocalDateTime.now();
        if(this.status == null) this.status = TransactionStatus.EXECUTED;
    }

    public TransactionState getState() {
        if(state == null) {
            state = switch (status) {
                 case PENDING -> new PendingState();
                 case VALIDATED ->  new ValidatedState();
                 case EXECUTED ->  new ExecutedState();
                 case REJECTED ->  new RejectedState();
                 case REVERSED ->  new ReversedState();
            };
        }

        return state;
    }

    public void advanceTo(TransactionState newState) {
        state = newState;
        status = newState.status();
    }

    public void markAsExecuted() {
        domainEvents.add(new TransactionExecutedEvent(
                id,
                type.name(),
                sourceAccountId,
                targetAccountId,
                amount,
                fee
        ));
    }

    public List<Object> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    public void clearDomainEvents() {
        domainEvents.clear();
    }

    public void executeTransfer() {
        advanceTo(getState().validate());
        advanceTo(getState().execute());
        markAsExecuted();
    }
}
