package com.banking.fintech.constant;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorInfo {

    INTERNAL_SERVER_ERROR(
            "An internal server error occurred.",
            "BANKING_001",
            HttpStatus.INTERNAL_SERVER_ERROR
    ),
    UNKNOWN_SERVER_ERROR(
            "An unknown server error occurred.",
            "BANKING_002",
            HttpStatus.INTERNAL_SERVER_ERROR
    );

    private final String errMsg;
    private final String errCode;
    private final HttpStatus httpStatus;

    ErrorInfo(String errMsg, String errCode, HttpStatus httpStatus) {
        this.errMsg = errMsg;
        this.errCode = errCode;
        this.httpStatus = httpStatus;
    }
}
