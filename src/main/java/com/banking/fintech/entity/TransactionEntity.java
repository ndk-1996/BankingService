package com.banking.fintech.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "transactions")
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long transactionId;

    @ManyToOne
    @JoinColumn(
            name = "account_id",
            foreignKey = @ForeignKey(name = "fk_account"),
            nullable = false
    )
    private AccountEntity accountEntity;

    @ManyToOne
    @JoinColumn(
            name = "operation_type_id",
            foreignKey = @ForeignKey(name = "fk_operation_type"),
            nullable = false
    )
    private OperationTypeEntity operationTypeEntity;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "balance", nullable = false)
    private Double balance;

    @Column(name = "event_date", nullable = false)
    private Instant eventDate;
}
