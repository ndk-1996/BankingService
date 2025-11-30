package com.banking.fintech.service;

import com.banking.fintech.constant.ErrorInfo;
import com.banking.fintech.dto.AccountReq;
import com.banking.fintech.dto.AccountRes;
import com.banking.fintech.entity.AccountEntity;
import com.banking.fintech.exception.AccountServiceException;
import com.banking.fintech.repo.AccountRepository;
import jakarta.persistence.PersistenceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public AccountRes createAccount(AccountReq accountReq) {
        log.info("In createAccount with req: {}", accountReq);

        AccountEntity accountEntity = AccountEntity.builder()
                .documentNumber(accountReq.getDocumentNumber())
                .build();

        try {
            accountEntity = accountRepository.save(accountEntity);
            log.info("Saved account entity successfully to the db with entity: {}", accountEntity);
        } catch (PersistenceException e) {
            log.error("Error while saving account entity to the db", e);
            throw new AccountServiceException(ErrorInfo.INTERNAL_SERVER_ERROR_WHILE_SAVING_TO_DB, e);
        }

        return AccountRes.builder()
                .accountId(accountEntity.getAccountId())
                .documentNumber(accountEntity.getDocumentNumber())
                .build();
    }

    @Override
    public AccountRes getAccount(Long accountId) {
        log.info("In getAccount with accountId: {}", accountId);
        try {
            boolean exists = accountRepository.existsById(accountId);
            AccountEntity accountEntity;
            if (exists) {
                accountEntity = accountRepository.getReferenceById(accountId);
            } else {
                log.error("Customer account not found for accountId: {}", accountId);
                throw new AccountServiceException(ErrorInfo.CUSTOMER_ACCOUNT_NOT_FOUND);
            }

            return AccountRes.builder()
                    .accountId(accountEntity.getAccountId())
                    .documentNumber(accountEntity.getDocumentNumber())
                    .build();
        } catch (PersistenceException e) {
            log.error("Error while getting account entity from the db", e);
            throw new AccountServiceException(ErrorInfo.INTERNAL_SERVER_ERROR_WHILE_GETTING_FROM_DB, e);
        }
    }
}
