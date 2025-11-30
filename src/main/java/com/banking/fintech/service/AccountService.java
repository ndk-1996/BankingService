package com.banking.fintech.service;

import com.banking.fintech.dto.AccountReq;
import com.banking.fintech.dto.AccountRes;

public interface AccountService {

    AccountRes createAccount(AccountReq accountReq);

    AccountRes getAccount(Long accountId);
}
