package com.banking.fintech.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TransactionReq {

    @NotNull(message = "AccountId is required.")
    private Long accountId;

    @NotNull(message = "OperationTypeId is required.")
    private Long operationTypeId;

    @NotNull(message = "Amount is required.")
    private Double amount;
}
