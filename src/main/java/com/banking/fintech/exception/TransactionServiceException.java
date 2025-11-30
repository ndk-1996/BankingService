package com.banking.fintech.exception;

import com.banking.fintech.constant.ErrorInfo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Getter
@ToString
public class TransactionServiceException extends BankingServiceException {

    public TransactionServiceException(ErrorInfo errorInfo) {
        super(errorInfo);
    }

    public TransactionServiceException(ErrorInfo errorInfo, Throwable cause) {
        super(errorInfo, cause);
    }
}
