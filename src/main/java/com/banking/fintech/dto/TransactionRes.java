package com.banking.fintech.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TransactionRes {

    private Long transactionId;
    private Long accountId;
    private Long operationTypeId;
    private Double amount;
    private Timestamp eventDate;
}
