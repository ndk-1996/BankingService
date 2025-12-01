package com.banking.fintech.controller;

import com.banking.fintech.dto.TransactionReq;
import com.banking.fintech.dto.TransactionRes;
import com.banking.fintech.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("TransactionControllerImpl Tests")
class TransactionControllerImplTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionControllerImpl transactionController;

    private TransactionReq transactionReq;
    private TransactionRes transactionRes;

    @BeforeEach
    void setUp() {
        transactionReq = TransactionReq.builder()
                .accountId(1L)
                .operationTypeId(1L)
                .amount(100.0)
                .build();

        transactionRes = TransactionRes.builder()
                .transactionId(1L)
                .accountId(1L)
                .operationTypeId(1L)
                .amount(100.0)
                .eventDate(Instant.now())
                .build();
    }

    @Test
    @DisplayName("Should create transaction successfully")
    void testCreateTransactionSuccess() {
        // Arrange
        when(transactionService.createTransaction(any(TransactionReq.class)))
                .thenReturn(transactionRes);

        // Act
        ResponseEntity<TransactionRes> response = transactionController.createTransaction(transactionReq);

        // Assert
        assertThat(response)
                .isNotNull()
                .extracting(ResponseEntity::getStatusCode)
                .isEqualTo(HttpStatus.OK);

        assertThat(response.getBody())
                .isNotNull()
                .extracting(TransactionRes::getTransactionId, TransactionRes::getAccountId, TransactionRes::getOperationTypeId, TransactionRes::getAmount)
                .containsExactly(1L, 1L, 1L, 100.0);

        verify(transactionService).createTransaction(transactionReq);
    }

    @Test
    @DisplayName("Should handle transaction with different account ID")
    void testCreateTransactionWithDifferentAccountId() {
        // Arrange
        TransactionReq customReq = transactionReq.toBuilder()
                .accountId(2L)
                .build();

        TransactionRes customRes = transactionRes.toBuilder()
                .accountId(2L)
                .build();

        when(transactionService.createTransaction(any(TransactionReq.class)))
                .thenReturn(customRes);

        // Act
        ResponseEntity<TransactionRes> response = transactionController.createTransaction(customReq);

        // Assert
        assertThat(response.getBody())
                .isNotNull()
                .extracting(TransactionRes::getAccountId)
                .isEqualTo(2L);

        verify(transactionService).createTransaction(customReq);
    }

    @Test
    @DisplayName("Should handle transaction with different operation type ID")
    void testCreateTransactionWithDifferentOperationTypeId() {
        // Arrange
        TransactionReq customReq = transactionReq.toBuilder()
                .operationTypeId(2L)
                .build();

        TransactionRes customRes = transactionRes.toBuilder()
                .operationTypeId(2L)
                .build();

        when(transactionService.createTransaction(any(TransactionReq.class)))
                .thenReturn(customRes);

        // Act
        ResponseEntity<TransactionRes> response = transactionController.createTransaction(customReq);

        // Assert
        assertThat(response.getBody())
                .isNotNull()
                .extracting(TransactionRes::getOperationTypeId)
                .isEqualTo(2L);

        verify(transactionService).createTransaction(customReq);
    }

    @Test
    @DisplayName("Should handle transaction with different amount")
    void testCreateTransactionWithDifferentAmount() {
        // Arrange
        TransactionReq customReq = transactionReq.toBuilder()
                .amount(250.50)
                .build();

        TransactionRes customRes = transactionRes.toBuilder()
                .amount(250.50)
                .build();

        when(transactionService.createTransaction(any(TransactionReq.class)))
                .thenReturn(customRes);

        // Act
        ResponseEntity<TransactionRes> response = transactionController.createTransaction(customReq);

        // Assert
        assertThat(response.getBody())
                .isNotNull()
                .extracting(TransactionRes::getAmount)
                .isEqualTo(250.50);

        verify(transactionService).createTransaction(customReq);
    }

    @Test
    @DisplayName("Should handle transaction with large amount")
    void testCreateTransactionWithLargeAmount() {
        // Arrange
        TransactionReq customReq = transactionReq.toBuilder()
                .amount(999999.99)
                .build();

        TransactionRes customRes = transactionRes.toBuilder()
                .amount(999999.99)
                .build();

        when(transactionService.createTransaction(any(TransactionReq.class)))
                .thenReturn(customRes);

        // Act
        ResponseEntity<TransactionRes> response = transactionController.createTransaction(customReq);

        // Assert
        assertThat(response.getBody())
                .isNotNull()
                .extracting(TransactionRes::getAmount)
                .isEqualTo(999999.99);
    }

    @Test
    @DisplayName("Should handle transaction with small amount")
    void testCreateTransactionWithSmallAmount() {
        // Arrange
        TransactionReq customReq = transactionReq.toBuilder()
                .amount(0.01)
                .build();

        TransactionRes customRes = transactionRes.toBuilder()
                .amount(0.01)
                .build();

        when(transactionService.createTransaction(any(TransactionReq.class)))
                .thenReturn(customRes);

        // Act
        ResponseEntity<TransactionRes> response = transactionController.createTransaction(customReq);

        // Assert
        assertThat(response.getBody())
                .isNotNull()
                .extracting(TransactionRes::getAmount)
                .isEqualTo(0.01);
    }

    @Test
    @DisplayName("Should return correct response entity status code")
    void testCreateTransactionResponseStatusCode() {
        // Arrange
        when(transactionService.createTransaction(any(TransactionReq.class)))
                .thenReturn(transactionRes);

        // Act
        ResponseEntity<TransactionRes> response = transactionController.createTransaction(transactionReq);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("Should return response with all transaction details")
    void testCreateTransactionAllDetailsReturned() {
        // Arrange
        Instant eventDate = Instant.now();
        TransactionRes expectedRes = TransactionRes.builder()
                .transactionId(5L)
                .accountId(3L)
                .operationTypeId(2L)
                .amount(500.0)
                .eventDate(eventDate)
                .build();

        when(transactionService.createTransaction(any(TransactionReq.class)))
                .thenReturn(expectedRes);

        // Act
        ResponseEntity<TransactionRes> response = transactionController.createTransaction(transactionReq);

        // Assert
        assertThat(response.getBody())
                .isNotNull()
                .satisfies(res -> {
                    assertThat(res.getTransactionId()).isEqualTo(5L);
                    assertThat(res.getAccountId()).isEqualTo(3L);
                    assertThat(res.getOperationTypeId()).isEqualTo(2L);
                    assertThat(res.getAmount()).isEqualTo(500.0);
                    assertThat(res.getEventDate()).isEqualTo(eventDate);
                });
    }

    @Test
    @DisplayName("Should return non-null response entity")
    void testCreateTransactionResponseNotNull() {
        // Arrange
        when(transactionService.createTransaction(any(TransactionReq.class)))
                .thenReturn(transactionRes);

        // Act
        ResponseEntity<TransactionRes> response = transactionController.createTransaction(transactionReq);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @DisplayName("Should handle transaction with high account ID")
    void testCreateTransactionWithHighAccountId() {
        // Arrange
        TransactionReq customReq = transactionReq.toBuilder()
                .accountId(Long.MAX_VALUE)
                .build();

        TransactionRes customRes = transactionRes.toBuilder()
                .accountId(Long.MAX_VALUE)
                .build();

        when(transactionService.createTransaction(any(TransactionReq.class)))
                .thenReturn(customRes);

        // Act
        ResponseEntity<TransactionRes> response = transactionController.createTransaction(customReq);

        // Assert
        assertThat(response.getBody())
                .isNotNull()
                .extracting(TransactionRes::getAccountId)
                .isEqualTo(Long.MAX_VALUE);
    }

    @Test
    @DisplayName("Should handle transaction with high operation type ID")
    void testCreateTransactionWithHighOperationTypeId() {
        // Arrange
        TransactionReq customReq = transactionReq.toBuilder()
                .operationTypeId(Long.MAX_VALUE)
                .build();

        TransactionRes customRes = transactionRes.toBuilder()
                .operationTypeId(Long.MAX_VALUE)
                .build();

        when(transactionService.createTransaction(any(TransactionReq.class)))
                .thenReturn(customRes);

        // Act
        ResponseEntity<TransactionRes> response = transactionController.createTransaction(customReq);

        // Assert
        assertThat(response.getBody())
                .isNotNull()
                .extracting(TransactionRes::getOperationTypeId)
                .isEqualTo(Long.MAX_VALUE);
    }

    @Test
    @DisplayName("Should handle multiple sequential transactions")
    void testCreateMultipleTransactionsSequentially() {
        // Arrange
        TransactionReq req1 = transactionReq.toBuilder().accountId(1L).build();
        TransactionReq req2 = transactionReq.toBuilder().accountId(2L).build();

        TransactionRes res1 = transactionRes.toBuilder().accountId(1L).build();
        TransactionRes res2 = transactionRes.toBuilder().accountId(2L).build();

        when(transactionService.createTransaction(req1)).thenReturn(res1);
        when(transactionService.createTransaction(req2)).thenReturn(res2);

        // Act
        ResponseEntity<TransactionRes> response1 = transactionController.createTransaction(req1);
        ResponseEntity<TransactionRes> response2 = transactionController.createTransaction(req2);

        // Assert
        assertThat(response1.getBody()).isNotNull().extracting(TransactionRes::getAccountId).isEqualTo(1L);
        assertThat(response2.getBody()).isNotNull().extracting(TransactionRes::getAccountId).isEqualTo(2L);

        verify(transactionService).createTransaction(req1);
        verify(transactionService).createTransaction(req2);
    }

    @Test
    @DisplayName("Should handle transaction with specific transaction ID in response")
    void testCreateTransactionWithSpecificTransactionId() {
        // Arrange
        long transactionId = 12345L;
        TransactionRes customRes = transactionRes.toBuilder()
                .transactionId(transactionId)
                .build();

        when(transactionService.createTransaction(any(TransactionReq.class)))
                .thenReturn(customRes);

        // Act
        ResponseEntity<TransactionRes> response = transactionController.createTransaction(transactionReq);

        // Assert
        assertThat(response.getBody())
                .isNotNull()
                .extracting(TransactionRes::getTransactionId)
                .isEqualTo(transactionId);
    }
}
