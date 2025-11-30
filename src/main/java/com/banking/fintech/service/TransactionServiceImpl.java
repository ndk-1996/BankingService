package com.banking.fintech.service;

import com.banking.fintech.constant.ErrorInfo;
import com.banking.fintech.dto.TransactionReq;
import com.banking.fintech.dto.TransactionRes;
import com.banking.fintech.entity.OperationTypeEntity;
import com.banking.fintech.entity.TransactionEntity;
import com.banking.fintech.exception.TransactionServiceException;
import com.banking.fintech.repo.OperationTypeRepository;
import com.banking.fintech.repo.TransactionRepository;
import jakarta.persistence.PersistenceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final OperationTypeRepository operationTypeRepository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository, OperationTypeRepository operationTypeRepository) {
        this.transactionRepository = transactionRepository;
        this.operationTypeRepository = operationTypeRepository;
    }

    @Override
    public TransactionRes createTransaction(TransactionReq transactionReq) {
        log.info("In createTransaction with transactionReq: {}", transactionReq);
        log.info("Validating and getting operation type from the db for the provided operationTypeId");

        try {
            boolean exists = operationTypeRepository.existsById(transactionReq.getOperationTypeId());
            if (exists) {
                OperationTypeEntity operationTypeEntity = operationTypeRepository.getReferenceById(transactionReq.getOperationTypeId());
                Double amount = transactionReq.getAmount() * operationTypeEntity.getOperationType().getMultiplier();
                transactionReq.setAmount(amount);
            } else {
                log.error("Transaction operation type not found for the provided operationTypeId: {}", transactionReq.getOperationTypeId());
                throw new TransactionServiceException(ErrorInfo.TRANSACTION_OPERATION_TYPE_NOT_FOUND);
            }
        } catch (PersistenceException e) {
            log.error("Error while getting operation type entity from the db", e);
            throw new TransactionServiceException(ErrorInfo.INTERNAL_SERVER_ERROR_WHILE_GETTING_FROM_DB, e);
        }

        TransactionEntity transactionEntity = TransactionEntity.builder()
                .accountId(transactionReq.getAccountId())
                .operationTypeId(transactionReq.getOperationTypeId())
                .amount(transactionReq.getAmount())
                .eventDate(Instant.now())
                .build();

        try {
            transactionEntity = transactionRepository.save(transactionEntity);
            log.info("Saved transaction entity successfully to the db with entity: {}", transactionEntity);
        } catch (PersistenceException e) {
            log.error("Error while saving transaction entity to the db", e);
            throw new TransactionServiceException(ErrorInfo.INTERNAL_SERVER_ERROR_WHILE_SAVING_TO_DB, e);
        }

        return TransactionRes.builder()
                .transactionId(transactionEntity.getTransactionId())
                .operationTypeId(transactionEntity.getOperationTypeId())
                .amount(transactionEntity.getAmount())
                .eventDate(transactionEntity.getEventDate())
                .build();
    }
}
