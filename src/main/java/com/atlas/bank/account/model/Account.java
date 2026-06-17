package com.atlas.bank.account.model;

import com.atlas.bank.shared.model.Currency;
import com.atlas.bank.shared.model.Money;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "account_number", nullable = false, unique = true)
    private String accountNumber;

    @Column(name = "owner_name", nullable = false)
    private String ownerName;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AccounType type;

    @Embedded // Para almacenar el balance como un solo campo en la base de datos
    @AttributeOverrides({ // Para especificar el nombre de la columna en la base de datos
            @AttributeOverride(name = "amount", column = @Column(name = "balance_amount", nullable = false)),
            @AttributeOverride(name = "currency", column = @Column(name = "balance_currency", nullable = false, length = 3))
    })
    private Money balance;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if(this.status == null) {
            this.status = AccountStatus.ACTIVE;
        }
        if(this.balance == null) {
            this.balance = Money.zero(Currency.USD);
        }
    }
}
