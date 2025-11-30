package com.banking.fintech.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class AccountRes {

    @JsonProperty("account_id")
    private Long accountId;

    @JsonProperty("document_number")
    private String documentNumber;
}
