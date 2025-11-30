package com.banking.fintech.exception;

import com.banking.fintech.constant.ErrorInfo;
import com.banking.fintech.dto.ErrorDetailRes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            BankingServiceException.class,
            AccountServiceException.class,
            TransactionServiceException.class
    })
    public ResponseEntity<ErrorDetailRes> handleBankingServiceException(BankingServiceException bankingServiceException) {
        ErrorDetailRes errorDetailRes = ErrorDetailRes.builder()
                .errCode(bankingServiceException.getErrorInfo().getErrCode())
                .errMsg(bankingServiceException.getErrorInfo().getErrMsg())
                .build();

        return ResponseEntity.status(bankingServiceException.getErrorInfo().getHttpStatus().value())
                .body(errorDetailRes);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetailRes> handleGenericException(Exception exception) {
        ErrorInfo errorInfo = ErrorInfo.UNKNOWN_SERVER_ERROR;
        ErrorDetailRes errorDetailRes = ErrorDetailRes.builder()
                .errCode(errorInfo.getErrCode())
                .errMsg(errorInfo.getErrMsg() + " " + exception.getMessage())
                .build();

        return ResponseEntity.status(errorInfo.getHttpStatus().value())
                .body(errorDetailRes);
    }
}
