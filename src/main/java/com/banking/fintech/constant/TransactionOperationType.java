package com.banking.fintech.constant;

import com.banking.fintech.exception.TransactionServiceException;
import lombok.Getter;

@Getter
public enum TransactionOperationType {

    CREDIT("credit"),
    DEBIT("debit");

    private final String value;

    TransactionOperationType(String value) {
        this.value = value;
    }

    public boolean isCredit() {
        return CREDIT.equals(this);
    }

    public boolean isDebit() {
        return DEBIT.equals(this);
    }

    public static TransactionOperationType getTransactionOperationType(String operationType) {
        if (operationType != null) {
            for (TransactionOperationType transactionOperationType : values()) {
                if (transactionOperationType.value.equalsIgnoreCase(operationType)) {
                    return transactionOperationType;
                }
            }

            throw new TransactionServiceException();
        }

        throw new TransactionServiceException();
    }
}
