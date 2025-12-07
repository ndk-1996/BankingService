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
    INTERNAL_SERVER_ERROR_WHILE_SAVING_TO_DB(
            "An internal server error occurred, while saving to db.",
            "BANKING_002",
            HttpStatus.INTERNAL_SERVER_ERROR
    ),
    INTERNAL_SERVER_ERROR_WHILE_GETTING_FROM_DB(
            "An internal server error occurred, while getting from db.",
            "BANKING_003",
            HttpStatus.INTERNAL_SERVER_ERROR
    ),
    UNKNOWN_SERVER_ERROR(
            "An unknown server error occurred.",
            "BANKING_004",
            HttpStatus.INTERNAL_SERVER_ERROR
    ),
    UNSUPPORTED_TRANSACTION_OPERATION_TYPE_INTERNAL_ERROR(
            "The transaction operation type is not supported by the application.",
            "BANKING_TRANSACTION_005",
            HttpStatus.INTERNAL_SERVER_ERROR
    ),
    TRANSACTION_OPERATION_TYPE_CANNOT_BE_NULL(
            "The transaction operation type cannot be null.",
            "BANKING_TRANSACTION_006",
            HttpStatus.INTERNAL_SERVER_ERROR
    ),
    FAILURE_WHILE_DISCHARGING_BALANCE(
            "An error occurred while discharging the balance.",
            "BANKING_TRANSACTION_007",
            HttpStatus.INTERNAL_SERVER_ERROR
    ),
    DOCUMENT_NUMBER_SHOULD_ONLY_CONSIST_OF_DIGITS(
            "The document number should only consist of digits.",
            "BANKING_ACCOUNT_008",
            HttpStatus.BAD_REQUEST
    ),
    CUSTOMER_ACCOUNT_NOT_FOUND(
            "Customer account not found for the provided accountId.",
            "BANKING_ACCOUNT_009",
            HttpStatus.NOT_FOUND
    ),
    TRANSACTION_OPERATION_TYPE_NOT_FOUND(
            "Transaction operation type not found for the provided operationTypeId.",
            "BANKING_ACCOUNT_010",
            HttpStatus.NOT_FOUND
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
