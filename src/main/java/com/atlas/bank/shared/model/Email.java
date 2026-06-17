package com.atlas.bank.shared.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable // Says to JPA doesn't create a table for this class.
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class Email {
    @Column(nullable = false, unique = true)
    private String value;

    public Email(String value) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException("Email cannot be null or blank");

        if(!value.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("Email must be a valid email address");
        }

        this.value = value.trim().toLowerCase();
    }

    public static Email of(String value) {
        return new Email(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
