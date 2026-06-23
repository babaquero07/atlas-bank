package com.atlas.bank.domain.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AccountClosedEvent(
        Long accountId,
        String accountNumber,
        String ownerName,
        BigDecimal balanceAtClose,
        LocalDateTime closedAt
) { }
