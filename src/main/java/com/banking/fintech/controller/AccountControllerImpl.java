package com.banking.fintech.controller;

import com.banking.fintech.dto.AccountReq;
import com.banking.fintech.dto.AccountRes;
import com.banking.fintech.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class AccountControllerImpl implements AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountControllerImpl(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public ResponseEntity<AccountRes> createAccount(AccountReq accountReq) {
        return null;
    }

    @Override
    public ResponseEntity<AccountRes> getAccount(Long accountId) {
        return null;
    }
}
