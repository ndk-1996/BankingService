package com.banking.fintech.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TransactionReq {

    @NotNull(message = "account_id is required")
    @Positive(message = "account_id must be greater than 0")
    @JsonProperty("account_id")
    private Long accountId;

    @NotNull(message = "operation_type_id is required")
    @Positive(message = "operation_type_id must be greater than 0")
    @JsonProperty("operation_type_id")
    private Long operationTypeId;

    @NotNull(message = "amount is required")
    @Positive(message = "amount must be greater than 0")
    @JsonProperty("amount")
    private Double amount;
}
