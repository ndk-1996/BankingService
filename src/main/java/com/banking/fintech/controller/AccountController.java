package com.banking.fintech.controller;

import com.banking.fintech.dto.AccountReq;
import com.banking.fintech.dto.AccountRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Accounts API", description = "Endpoints for customer accounts related operations")
@RequestMapping("/accounts")
public interface AccountController {

    @Operation(summary = "Create account for a customer")
    @PostMapping()
    ResponseEntity<AccountRes> createAccount(@Valid @RequestBody AccountReq accountReq);

    @Operation(summary = "Get account details for a customer")
    @GetMapping("/{accountId}")
    ResponseEntity<AccountRes> getAccount(@PathVariable Long accountId);
}
