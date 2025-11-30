package com.banking.fintech.entity;

import com.banking.fintech.constant.TransactionOperationType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TransactionOperationTypeConverter implements AttributeConverter<TransactionOperationType, String> {

    @Override
    public String convertToDatabaseColumn(TransactionOperationType operationType) {
        return operationType != null ? operationType.getValue() : null;
    }

    @Override
    public TransactionOperationType convertToEntityAttribute(String operationType) {
        return operationType != null ? TransactionOperationType.getTransactionOperationType(operationType) : null;
    }
}
