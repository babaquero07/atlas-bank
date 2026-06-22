package com.atlas.bank.domain.model.shared;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailDomainTest {
    @Test
    @DisplayName("Debe crear email con formato valido")
    void shouldCreateValidEmail() {
        Email email = Email.of("abaquero@gmail.com");

        assertEquals("abaquero@gmail.com", email.getValue());
    }

    @Test
    @DisplayName("Debe normalizar a minusculas")
    void shouldNormalizeEmail() {
        Email email = Email.of("ABAQUERO@GMAIL.COM");

        assertEquals("abaquero@gmail.com", email.getValue());
    }

    @Test
    @DisplayName("Debe rechazar email con formato invalido")
    void shouldRejectEmailWithInvalidFormat() {
        assertThrows(IllegalArgumentException.class, () -> Email.of("no=es=correo"));
    }

    @Test
    @DisplayName("Debe rechazar email nulo")
    void shouldRejectEmailWithNullValue() {
        assertThrows(IllegalArgumentException.class, () -> Email.of(null));
    }
}