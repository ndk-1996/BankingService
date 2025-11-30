package com.banking.fintech.controller;

import com.banking.fintech.dto.TransactionReq;
import com.banking.fintech.dto.TransactionRes;
import com.banking.fintech.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TransactionControllerImpl implements TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionControllerImpl(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    public ResponseEntity<TransactionRes> createTransaction(TransactionReq transactionReq) {
        log.info("In createTransaction with transactionReq: {}", transactionReq);

        return ResponseEntity.ok(transactionService.createTransaction(transactionReq));
    }
}
