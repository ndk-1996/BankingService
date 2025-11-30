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
    ),
    UNSUPPORTED_TRANSACTION_OPERATION_TYPE_INTERNAL_ERROR(
            "The transaction operation type is not supported by the application.",
            "BANKING_TRANSACTION_003",
            HttpStatus.INTERNAL_SERVER_ERROR
    ),
    TRANSACTION_OPERATION_TYPE_CANNOT_BE_NULL(
            "The transaction operation type cannot be null.",
            "BANKING_TRANSACTION_004",
            HttpStatus.INTERNAL_SERVER_ERROR
    ),
    DOCUMENT_NUMBER_SHOULD_ONLY_CONSIST_OF_DIGITS(
            "The document number should only consist of digits.",
            "BANKING_TRANSACTION_005",
            HttpStatus.BAD_REQUEST
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
