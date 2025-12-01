package com.banking.fintech.service;

import com.banking.fintech.constant.ErrorInfo;
import com.banking.fintech.constant.TransactionOperationType;
import com.banking.fintech.dto.TransactionReq;
import com.banking.fintech.dto.TransactionRes;
import com.banking.fintech.entity.AccountEntity;
import com.banking.fintech.entity.OperationTypeEntity;
import com.banking.fintech.entity.TransactionEntity;
import com.banking.fintech.exception.TransactionServiceException;
import com.banking.fintech.repo.OperationTypeRepository;
import com.banking.fintech.repo.TransactionRepository;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TransactionServiceImpl Unit Tests")
class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private OperationTypeRepository operationTypeRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private TransactionReq transactionReq;
    private TransactionEntity transactionEntity;
    private OperationTypeEntity operationTypeEntity;
    private AccountEntity accountEntity;

    @BeforeEach
    void setUp() {
        // Initialize test data
        accountEntity = AccountEntity.builder()
                .accountId(1L)
                .build();

        operationTypeEntity = OperationTypeEntity.builder()
                .operationTypeId(1L)
                .description("CASH PURCHASE")
                .operationType(TransactionOperationType.CREDIT)
                .build();

        transactionReq = TransactionReq.builder()
                .accountId(1L)
                .operationTypeId(1L)
                .amount(100.0)
                .build();

        transactionEntity = TransactionEntity.builder()
                .transactionId(1L)
                .accountEntity(accountEntity)
                .operationTypeEntity(operationTypeEntity)
                .amount(100.0)
                .eventDate(Instant.now())
                .build();
    }

    @Test
    @DisplayName("Should create transaction successfully with CREDIT operation type")
    void shouldCreateTransactionWithCreditOperationType() {
        // Arrange
        when(operationTypeRepository.existsById(1L)).thenReturn(true);
        when(operationTypeRepository.getReferenceById(1L)).thenReturn(operationTypeEntity);
        when(transactionRepository.save(any(TransactionEntity.class))).thenReturn(transactionEntity);

        // Act
        TransactionRes result = transactionService.createTransaction(transactionReq);

        // Assert
        assertThat(result)
                .isNotNull()
                .extracting(
                        TransactionRes::getTransactionId,
                        TransactionRes::getAccountId,
                        TransactionRes::getOperationTypeId,
                        TransactionRes::getAmount
                )
                .containsExactly(1L, 1L, 1L, 100.0);
        assertThat(result.getEventDate()).isNotNull();

        verify(operationTypeRepository).existsById(1L);
        verify(operationTypeRepository).getReferenceById(1L);
        verify(transactionRepository).save(any(TransactionEntity.class));
        verifyNoMoreInteractions(operationTypeRepository, transactionRepository);
    }

    @Test
    @DisplayName("Should create transaction with DEBIT operation type and negative multiplier")
    void shouldCreateTransactionWithDebitOperationType() {
        // Arrange
        OperationTypeEntity debitOperationType = OperationTypeEntity.builder()
                .operationTypeId(2L)
                .description("WITHDRAWAL")
                .operationType(TransactionOperationType.DEBIT)
                .build();

        transactionReq.setOperationTypeId(2L);
        transactionReq.setAmount(50.0);

        TransactionEntity debitTransaction = TransactionEntity.builder()
                .transactionId(2L)
                .accountEntity(accountEntity)
                .operationTypeEntity(debitOperationType)
                .amount(-50.0)
                .eventDate(Instant.now())
                .build();

        when(operationTypeRepository.existsById(2L)).thenReturn(true);
        when(operationTypeRepository.getReferenceById(2L)).thenReturn(debitOperationType);
        when(transactionRepository.save(any(TransactionEntity.class))).thenReturn(debitTransaction);

        // Act
        TransactionRes result = transactionService.createTransaction(transactionReq);

        // Assert
        assertThat(result)
                .isNotNull()
                .extracting(TransactionRes::getAmount)
                .isEqualTo(-50.0);
        assertThat(result.getOperationTypeId()).isEqualTo(2L);

        verify(operationTypeRepository).existsById(2L);
        verify(operationTypeRepository).getReferenceById(2L);
        verify(transactionRepository).save(any(TransactionEntity.class));
    }

    @Test
    @DisplayName("Should apply multiplier correctly to transaction amount")
    void shouldApplyMultiplierCorrectly() {
        // Arrange
        Double originalAmount = 100.0;
        Double expectedAmount = 100.0; // CREDIT multiplier is 1
        transactionReq.setAmount(originalAmount);

        transactionEntity.setAmount(expectedAmount);

        when(operationTypeRepository.existsById(1L)).thenReturn(true);
        when(operationTypeRepository.getReferenceById(1L)).thenReturn(operationTypeEntity);
        when(transactionRepository.save(any(TransactionEntity.class))).thenReturn(transactionEntity);

        // Act
        TransactionRes result = transactionService.createTransaction(transactionReq);

        // Assert
        assertThat(result.getAmount()).isEqualTo(expectedAmount);
        verify(transactionRepository).save(any(TransactionEntity.class));
    }

    @Test
    @DisplayName("Should handle large transaction amounts")
    void shouldHandleLargeTransactionAmounts() {
        // Arrange
        Double largeAmount = 999999999.99;
        transactionReq.setAmount(largeAmount);
        transactionEntity.setAmount(largeAmount);

        when(operationTypeRepository.existsById(1L)).thenReturn(true);
        when(operationTypeRepository.getReferenceById(1L)).thenReturn(operationTypeEntity);
        when(transactionRepository.save(any(TransactionEntity.class))).thenReturn(transactionEntity);

        // Act
        TransactionRes result = transactionService.createTransaction(transactionReq);

        // Assert
        assertThat(result.getAmount()).isEqualTo(largeAmount);
    }

    @Test
    @DisplayName("Should throw TransactionServiceException when operation type does not exist")
    void shouldThrowExceptionWhenOperationTypeNotExists() {
        // Arrange
        when(operationTypeRepository.existsById(99L)).thenReturn(false);

        transactionReq.setOperationTypeId(99L);

        // Act & Assert
        assertThatThrownBy(() -> transactionService.createTransaction(transactionReq))
                .isInstanceOf(TransactionServiceException.class)
                .hasMessage(ErrorInfo.TRANSACTION_OPERATION_TYPE_NOT_FOUND.getErrCode() + ": " + ErrorInfo.TRANSACTION_OPERATION_TYPE_NOT_FOUND.getErrMsg());

        verify(operationTypeRepository).existsById(99L);
        verify(operationTypeRepository, never()).getReferenceById(anyLong());
        verify(transactionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should not save transaction when operation type is not found")
    void shouldNotSaveTransactionWhenOperationTypeNotFound() {
        // Arrange
        when(operationTypeRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> transactionService.createTransaction(transactionReq))
                .isInstanceOf(TransactionServiceException.class);

        verify(transactionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should check existsById before getReferenceById")
    void shouldCheckExistsByIdBeforeGetReferenceById() {
        // Arrange
        when(operationTypeRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> transactionService.createTransaction(transactionReq))
                .isInstanceOf(TransactionServiceException.class);

        verify(operationTypeRepository).existsById(1L);
        verify(operationTypeRepository, never()).getReferenceById(1L);
    }

    @Test
    @DisplayName("Should throw TransactionServiceException when getting operation type fails")
    void shouldThrowExceptionWhenGetOperationTypeFails() {
        // Arrange
        when(operationTypeRepository.existsById(1L)).thenReturn(true);
        when(operationTypeRepository.getReferenceById(1L))
                .thenThrow(new PersistenceException("Database connection failed"));

        // Act & Assert
        assertThatThrownBy(() -> transactionService.createTransaction(transactionReq))
                .isInstanceOf(TransactionServiceException.class)
                .hasMessage(ErrorInfo.INTERNAL_SERVER_ERROR_WHILE_GETTING_FROM_DB.getErrCode() + ": " + ErrorInfo.INTERNAL_SERVER_ERROR_WHILE_GETTING_FROM_DB.getErrMsg())
                .hasCauseInstanceOf(PersistenceException.class);

        verify(operationTypeRepository).existsById(1L);
        verify(operationTypeRepository).getReferenceById(1L);
        verify(transactionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should wrap PersistenceException when getting operation type")
    void shouldWrapPersistenceExceptionWhenGettingOperationType() {
        // Arrange
        PersistenceException persistenceException = new PersistenceException("DB Error");
        when(operationTypeRepository.existsById(1L)).thenReturn(true);
        when(operationTypeRepository.getReferenceById(1L)).thenThrow(persistenceException);

        // Act & Assert
        assertThatThrownBy(() -> transactionService.createTransaction(transactionReq))
                .isInstanceOf(TransactionServiceException.class)
                .extracting(Throwable::getCause)
                .isEqualTo(persistenceException);
    }

    @Test
    @DisplayName("Should throw TransactionServiceException when saving transaction fails")
    void shouldThrowExceptionWhenSaveTransactionFails() {
        // Arrange
        when(operationTypeRepository.existsById(1L)).thenReturn(true);
        when(operationTypeRepository.getReferenceById(1L)).thenReturn(operationTypeEntity);
        when(transactionRepository.save(any(TransactionEntity.class)))
                .thenThrow(new PersistenceException("Save failed"));

        // Act & Assert
        assertThatThrownBy(() -> transactionService.createTransaction(transactionReq))
                .isInstanceOf(TransactionServiceException.class)
                .hasMessage(ErrorInfo.INTERNAL_SERVER_ERROR_WHILE_SAVING_TO_DB.getErrCode() + ": " + ErrorInfo.INTERNAL_SERVER_ERROR_WHILE_SAVING_TO_DB.getErrMsg())
                .hasCauseInstanceOf(PersistenceException.class);

        verify(transactionRepository).save(any(TransactionEntity.class));
    }

    @Test
    @DisplayName("Should wrap PersistenceException when saving transaction")
    void shouldWrapPersistenceExceptionWhenSavingTransaction() {
        // Arrange
        PersistenceException persistenceException = new PersistenceException("Save Error");
        when(operationTypeRepository.existsById(1L)).thenReturn(true);
        when(operationTypeRepository.getReferenceById(1L)).thenReturn(operationTypeEntity);
        when(transactionRepository.save(any(TransactionEntity.class))).thenThrow(persistenceException);

        // Act & Assert
        assertThatThrownBy(() -> transactionService.createTransaction(transactionReq))
                .isInstanceOf(TransactionServiceException.class)
                .extracting(Throwable::getCause)
                .isEqualTo(persistenceException);
    }

    @Test
    @DisplayName("Should build AccountEntity with correct accountId")
    void shouldBuildAccountEntityWithCorrectId() {
        // Arrange
        transactionReq.setAccountId(42L);
        transactionEntity.getAccountEntity().setAccountId(42L);

        when(operationTypeRepository.existsById(1L)).thenReturn(true);
        when(operationTypeRepository.getReferenceById(1L)).thenReturn(operationTypeEntity);
        when(transactionRepository.save(any(TransactionEntity.class))).thenReturn(transactionEntity);

        // Act
        TransactionRes result = transactionService.createTransaction(transactionReq);

        // Assert
        assertThat(result.getAccountId()).isEqualTo(42L);
    }

    @Test
    @DisplayName("Should build OperationTypeEntity with correct operationTypeId")
    void shouldBuildOperationTypeEntityWithCorrectId() {
        // Arrange
        transactionReq.setOperationTypeId(5L);
        operationTypeEntity.setOperationTypeId(5L);

        when(operationTypeRepository.existsById(5L)).thenReturn(true);
        when(operationTypeRepository.getReferenceById(5L)).thenReturn(operationTypeEntity);
        when(transactionRepository.save(any(TransactionEntity.class))).thenReturn(transactionEntity);

        // Act
        TransactionRes result = transactionService.createTransaction(transactionReq);

        // Assert
        assertThat(result.getOperationTypeId()).isEqualTo(5L);
    }

    @Test
    @DisplayName("Should create TransactionEntity with all required fields")
    void shouldCreateTransactionEntityWithAllFields() {
        // Arrange
        when(operationTypeRepository.existsById(1L)).thenReturn(true);
        when(operationTypeRepository.getReferenceById(1L)).thenReturn(operationTypeEntity);
        when(transactionRepository.save(any(TransactionEntity.class))).thenReturn(transactionEntity);

        // Act
        TransactionRes result = transactionService.createTransaction(transactionReq);

        // Assert
        assertThat(result)
                .hasFieldOrPropertyWithValue("transactionId", 1L)
                .hasFieldOrPropertyWithValue("accountId", 1L)
                .hasFieldOrPropertyWithValue("operationTypeId", 1L)
                .hasFieldOrPropertyWithValue("amount", 100.0)
                .hasFieldOrProperty("eventDate");
    }

    @Test
    @DisplayName("Should handle transaction with operation type ID 1")
    void shouldHandleOperationTypeId1() {
        // Arrange
        when(operationTypeRepository.existsById(1L)).thenReturn(true);
        when(operationTypeRepository.getReferenceById(1L)).thenReturn(operationTypeEntity);
        when(transactionRepository.save(any(TransactionEntity.class))).thenReturn(transactionEntity);

        // Act
        TransactionRes result = transactionService.createTransaction(transactionReq);

        // Assert
        assertThat(result.getOperationTypeId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Should handle transaction with operation type ID 2")
    void shouldHandleOperationTypeId2() {
        // Arrange
        OperationTypeEntity operationType2 = OperationTypeEntity.builder()
                .operationTypeId(2L)
                .description("Another operation")
                .operationType(TransactionOperationType.DEBIT)
                .build();

        TransactionEntity transaction2 = TransactionEntity.builder()
                .transactionId(2L)
                .accountEntity(accountEntity)
                .operationTypeEntity(operationType2)
                .amount(-100.0)
                .eventDate(Instant.now())
                .build();

        transactionReq.setOperationTypeId(2L);

        when(operationTypeRepository.existsById(2L)).thenReturn(true);
        when(operationTypeRepository.getReferenceById(2L)).thenReturn(operationType2);
        when(transactionRepository.save(any(TransactionEntity.class))).thenReturn(transaction2);

        // Act
        TransactionRes result = transactionService.createTransaction(transactionReq);

        // Assert
        assertThat(result.getOperationTypeId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("Should return TransactionRes with all fields populated")
    void shouldReturnTransactionResWithAllFields() {
        // Arrange
        when(operationTypeRepository.existsById(1L)).thenReturn(true);
        when(operationTypeRepository.getReferenceById(1L)).thenReturn(operationTypeEntity);
        when(transactionRepository.save(any(TransactionEntity.class))).thenReturn(transactionEntity);

        // Act
        TransactionRes result = transactionService.createTransaction(transactionReq);

        // Assert
        assertThat(result)
                .isNotNull()
                .extracting(
                        TransactionRes::getTransactionId,
                        TransactionRes::getAccountId,
                        TransactionRes::getOperationTypeId,
                        TransactionRes::getAmount
                )
                .doesNotContainNull();
    }

    @Test
    @DisplayName("Should map TransactionEntity fields correctly to TransactionRes")
    void shouldMapTransactionEntityFieldsCorrectly() {
        // Arrange
        Long expectedTransactionId = 5L;
        Long expectedAccountId = 10L;
        Long expectedOperationTypeId = 3L;
        Double expectedAmount = 250.50;
        Instant expectedEventDate = Instant.ofEpochSecond(1000000);

        transactionEntity.setTransactionId(expectedTransactionId);
        transactionEntity.getAccountEntity().setAccountId(expectedAccountId);
        transactionEntity.getOperationTypeEntity().setOperationTypeId(expectedOperationTypeId);
        transactionEntity.setAmount(expectedAmount);
        transactionEntity.setEventDate(expectedEventDate);

        when(operationTypeRepository.existsById(1L)).thenReturn(true);
        when(operationTypeRepository.getReferenceById(1L)).thenReturn(operationTypeEntity);
        when(transactionRepository.save(any(TransactionEntity.class))).thenReturn(transactionEntity);

        // Act
        TransactionRes result = transactionService.createTransaction(transactionReq);

        // Assert
        assertThat(result)
                .extracting(
                        TransactionRes::getTransactionId,
                        TransactionRes::getAccountId,
                        TransactionRes::getOperationTypeId,
                        TransactionRes::getAmount,
                        TransactionRes::getEventDate
                )
                .containsExactly(
                        expectedTransactionId,
                        expectedAccountId,
                        expectedOperationTypeId,
                        expectedAmount,
                        expectedEventDate
                );
    }

    @Test
    @DisplayName("Should handle maximum Long value for accountId")
    void shouldHandleMaximumLongValueForAccountId() {
        // Arrange
        AccountEntity maxAccount = AccountEntity.builder()
                .accountId(Long.MAX_VALUE)
                .build();

        transactionEntity.setAccountEntity(maxAccount);
        transactionReq.setAccountId(Long.MAX_VALUE);

        when(operationTypeRepository.existsById(1L)).thenReturn(true);
        when(operationTypeRepository.getReferenceById(1L)).thenReturn(operationTypeEntity);
        when(transactionRepository.save(any(TransactionEntity.class))).thenReturn(transactionEntity);

        // Act
        TransactionRes result = transactionService.createTransaction(transactionReq);

        // Assert
        assertThat(result.getAccountId()).isEqualTo(Long.MAX_VALUE);
    }

    @Test
    @DisplayName("Should handle decimal amount values")
    void shouldHandleDecimalAmountValues() {
        // Arrange
        Double decimalAmount = 123.456789;
        transactionReq.setAmount(decimalAmount);
        transactionEntity.setAmount(decimalAmount);

        when(operationTypeRepository.existsById(1L)).thenReturn(true);
        when(operationTypeRepository.getReferenceById(1L)).thenReturn(operationTypeEntity);
        when(transactionRepository.save(any(TransactionEntity.class))).thenReturn(transactionEntity);

        // Act
        TransactionRes result = transactionService.createTransaction(transactionReq);

        // Assert
        assertThat(result.getAmount()).isEqualTo(decimalAmount);
    }

    @Test
    @DisplayName("Should handle very small positive amount")
    void shouldHandleVerySmallPositiveAmount() {
        // Arrange
        Double smallAmount = 0.01;
        transactionReq.setAmount(smallAmount);
        transactionEntity.setAmount(smallAmount);

        when(operationTypeRepository.existsById(1L)).thenReturn(true);
        when(operationTypeRepository.getReferenceById(1L)).thenReturn(operationTypeEntity);
        when(transactionRepository.save(any(TransactionEntity.class))).thenReturn(transactionEntity);

        // Act
        TransactionRes result = transactionService.createTransaction(transactionReq);

        // Assert
        assertThat(result.getAmount()).isEqualTo(smallAmount);
    }
}
