package com.banking.fintech.entity;

import com.banking.fintech.constant.TransactionOperationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "operation_types")
public class OperationTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "operation_type_id")
    private Long operationTypeId;

    @Column(name = "description", nullable = false)
    private String description;

    @Convert(converter = TransactionOperationTypeConverter.class)
    @Column(name = "operation_type", nullable = false)
    private TransactionOperationType operationType;
}
