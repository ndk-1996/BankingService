package com.banking.fintech.controller;

import com.banking.fintech.dto.TransactionReq;
import com.banking.fintech.dto.TransactionRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Transactions API", description = "Endpoints for customer account transactions related operations")
@RequestMapping("/transactions")
public interface TransactionController {

    @Operation(summary = "Create transaction made by a customer account")
    @PostMapping()
    ResponseEntity<TransactionRes> createTransaction(@Valid @RequestBody TransactionReq transactionReq);
}
