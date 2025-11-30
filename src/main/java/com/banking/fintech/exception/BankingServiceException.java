package com.banking.fintech.exception;

import com.banking.fintech.constant.ErrorInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BankingServiceException extends RuntimeException {

    protected ErrorInfo errorInfo;
}
