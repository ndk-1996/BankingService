package com.banking.fintech.service;

import com.banking.fintech.dto.TransactionReq;
import com.banking.fintech.dto.TransactionRes;

public interface TransactionService {

    TransactionRes createTransaction(TransactionReq transactionReq);
}
