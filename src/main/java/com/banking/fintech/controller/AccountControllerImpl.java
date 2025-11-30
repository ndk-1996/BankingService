package com.banking.fintech.controller;

import com.banking.fintech.dto.AccountReq;
import com.banking.fintech.dto.AccountRes;
import com.banking.fintech.service.AccountService;
import com.banking.fintech.validator.AccountValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class AccountControllerImpl implements AccountController {

    private final AccountValidator accountValidator;
    private final AccountService accountService;

    @Autowired
    public AccountControllerImpl(AccountValidator accountValidator, AccountService accountService) {
        this.accountValidator = accountValidator;
        this.accountService = accountService;
    }

    @Override
    public ResponseEntity<AccountRes> createAccount(AccountReq accountReq) {
        log.info("In createAccount with req: {}", accountReq);
        accountValidator.validateCreateAccountReq(accountReq);

        return ResponseEntity.ok(accountService.createAccount(accountReq));
    }

    @Override
    public ResponseEntity<AccountRes> getAccount(Long accountId) {
        log.info("In getAccount with accountId: {}", accountId);

        return ResponseEntity.ok(accountService.getAccount(accountId));
    }
}
