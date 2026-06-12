package com.atlas.bank.transaction.service.listener;

import com.atlas.bank.transaction.service.event.TransactionExecutedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j // This annotation is used to log messages
public class NotificationListener {
    @EventListener
    public void onTransactionExecuted(TransactionExecutedEvent event) {
        log.info("Notification event: {}", event);
    }
}
