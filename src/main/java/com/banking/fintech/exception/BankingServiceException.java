package com.banking.fintech.exception;

import com.banking.fintech.constant.ErrorInfo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Getter
@ToString
public class BankingServiceException extends RuntimeException {

    protected ErrorInfo errorInfo;

    public BankingServiceException(ErrorInfo errorInfo) {
        super(errorInfo.getErrCode() + ": " + errorInfo.getErrMsg());
        this.errorInfo = errorInfo;
    }

    public BankingServiceException(ErrorInfo errorInfo, Throwable cause) {
        super(errorInfo.getErrCode() + ": " + errorInfo.getErrMsg(), cause);
        this.errorInfo = errorInfo;
    }
}
