package com.banking.fintech.exception;

import com.banking.fintech.constant.ErrorInfo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Getter
@ToString
public class AccountServiceException extends BankingServiceException {

    public AccountServiceException(ErrorInfo errorInfo) {
        super(errorInfo);
    }

    public AccountServiceException(ErrorInfo errorInfo, Throwable cause) {
        super(errorInfo, cause);
    }
}
