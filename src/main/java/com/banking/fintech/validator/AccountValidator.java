package com.banking.fintech.validator;

import com.banking.fintech.constant.ErrorInfo;
import com.banking.fintech.dto.AccountReq;
import com.banking.fintech.exception.AccountServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class AccountValidator {

    public void validateCreateAccountReq(AccountReq accountReq) {
        log.info("Validating document number for create account request with req: {}", accountReq);
        boolean areCharsDigits = true;
        for (int i = 0; i < accountReq.getDocumentNumber().length(); i++) {
            areCharsDigits = areCharsDigits & Character.isDigit(accountReq.getDocumentNumber().charAt(i));
        }

        if (!areCharsDigits) {
            throw new AccountServiceException(ErrorInfo.DOCUMENT_NUMBER_SHOULD_ONLY_CONSIST_OF_DIGITS);
        }
    }
}
