package com.atlas.bank.application.command;

public record CloseAccountCommand(
        Long accountId
) { }
