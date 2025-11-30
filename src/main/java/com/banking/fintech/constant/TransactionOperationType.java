package com.banking.fintech.constant;

import com.banking.fintech.exception.TransactionServiceException;
import lombok.Getter;

@Getter
public enum TransactionOperationType {

    CREDIT("credit", 1),
    DEBIT("debit", -1);

    private final String value;
    private final int multiplier;

    TransactionOperationType(String value, int multiplier) {
        this.value = value;
        this.multiplier = multiplier;
    }

    public static TransactionOperationType getTransactionOperationType(String operationType) {
        if (operationType != null) {
            for (TransactionOperationType transactionOperationType : values()) {
                if (transactionOperationType.value.equalsIgnoreCase(operationType)) {
                    return transactionOperationType;
                }
            }

            throw new TransactionServiceException(ErrorInfo.UNSUPPORTED_TRANSACTION_OPERATION_TYPE_INTERNAL_ERROR);
        }

        throw new TransactionServiceException(ErrorInfo.TRANSACTION_OPERATION_TYPE_CANNOT_BE_NULL);
    }
}
