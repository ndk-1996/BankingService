package com.banking.fintech.service;

import com.banking.fintech.constant.ErrorInfo;
import com.banking.fintech.dto.AccountReq;
import com.banking.fintech.dto.AccountRes;
import com.banking.fintech.entity.AccountEntity;
import com.banking.fintech.exception.AccountServiceException;
import com.banking.fintech.repo.AccountRepository;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AccountServiceImpl Unit Tests")
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    private AccountReq accountReq;
    private AccountEntity accountEntity;
    private AccountRes expectedAccountRes;

    @BeforeEach
    void setUp() {
        accountReq = AccountReq.builder()
                .documentNumber("12345678901")
                .build();

        accountEntity = AccountEntity.builder()
                .accountId(1L)
                .documentNumber("12345678901")
                .build();

        expectedAccountRes = AccountRes.builder()
                .accountId(1L)
                .documentNumber("12345678901")
                .build();
    }

    // ==================== createAccount Tests ====================

    @Test
    @DisplayName("createAccount - should successfully create an account and return AccountRes")
    void testCreateAccount_Success() {
        // Arrange
        when(accountRepository.save(any(AccountEntity.class))).thenReturn(accountEntity);

        // Act
        AccountRes result = accountService.createAccount(accountReq);

        // Assert
        assertThat(result)
                .isNotNull()
                .isEqualTo(expectedAccountRes);

        assertThat(result.getAccountId()).isEqualTo(1L);
        assertThat(result.getDocumentNumber()).isEqualTo("12345678901");

        verify(accountRepository, times(1)).save(any(AccountEntity.class));
    }

    @Test
    @DisplayName("createAccount - should throw AccountServiceException when PersistenceException occurs")
    void testCreateAccount_PersistenceException() {
        // Arrange
        PersistenceException persistenceException = new PersistenceException("Database error");
        when(accountRepository.save(any(AccountEntity.class)))
                .thenThrow(persistenceException);

        // Act & Assert
        assertThatThrownBy(() -> accountService.createAccount(accountReq))
                .isInstanceOf(AccountServiceException.class)
                .hasMessageContaining(ErrorInfo.INTERNAL_SERVER_ERROR_WHILE_SAVING_TO_DB.getErrMsg());

        verify(accountRepository, times(1)).save(any(AccountEntity.class));
    }

    @Test
    @DisplayName("createAccount - should use correct document number from request")
    void testCreateAccount_VerifyDocumentNumber() {
        // Arrange
        String documentNumber = "98765432101";
        accountReq.setDocumentNumber(documentNumber);
        accountEntity.setDocumentNumber(documentNumber);
        when(accountRepository.save(any(AccountEntity.class))).thenReturn(accountEntity);

        // Act
        AccountRes result = accountService.createAccount(accountReq);

        // Assert
        assertThat(result.getDocumentNumber()).isEqualTo(documentNumber);
        verify(accountRepository, times(1)).save(any(AccountEntity.class));
    }

    @Test
    @DisplayName("createAccount - should generate and return account ID")
    void testCreateAccount_GeneratedAccountId() {
        // Arrange
        accountEntity.setAccountId(999L);
        when(accountRepository.save(any(AccountEntity.class))).thenReturn(accountEntity);

        // Act
        AccountRes result = accountService.createAccount(accountReq);

        // Assert
        assertThat(result.getAccountId()).isEqualTo(999L);
        verify(accountRepository, times(1)).save(any(AccountEntity.class));
    }

    @Test
    @DisplayName("createAccount - should throw AccountServiceException with correct error info")
    void testCreateAccount_VerifyExceptionErrorInfo() {
        // Arrange
        when(accountRepository.save(any(AccountEntity.class)))
                .thenThrow(new PersistenceException("DB error"));

        // Act & Assert
        assertThatThrownBy(() -> accountService.createAccount(accountReq))
                .isInstanceOf(AccountServiceException.class)
                .extracting("errorInfo")
                .isEqualTo(ErrorInfo.INTERNAL_SERVER_ERROR_WHILE_SAVING_TO_DB);

        verify(accountRepository, times(1)).save(any(AccountEntity.class));
    }

    // ==================== getAccount Tests ====================

    @Test
    @DisplayName("getAccount - should successfully retrieve an account and return AccountRes")
    void testGetAccount_Success() {
        // Arrange
        Long accountId = 1L;
        when(accountRepository.existsById(accountId)).thenReturn(true);
        when(accountRepository.getReferenceById(accountId)).thenReturn(accountEntity);

        // Act
        AccountRes result = accountService.getAccount(accountId);

        // Assert
        assertThat(result)
                .isNotNull()
                .isEqualTo(expectedAccountRes);

        assertThat(result.getAccountId()).isEqualTo(1L);
        assertThat(result.getDocumentNumber()).isEqualTo("12345678901");

        verify(accountRepository, times(1)).existsById(accountId);
        verify(accountRepository, times(1)).getReferenceById(accountId);
    }

    @Test
    @DisplayName("getAccount - should throw AccountServiceException when account not found")
    void testGetAccount_AccountNotFound() {
        // Arrange
        Long accountId = 999L;
        when(accountRepository.existsById(accountId)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> accountService.getAccount(accountId))
                .isInstanceOf(AccountServiceException.class)
                .hasMessageContaining(ErrorInfo.CUSTOMER_ACCOUNT_NOT_FOUND.getErrMsg());

        verify(accountRepository, times(1)).existsById(accountId);
        verify(accountRepository, never()).getReferenceById(any());
    }

    @Test
    @DisplayName("getAccount - should throw AccountServiceException when PersistenceException occurs during existsById")
    void testGetAccount_PersistenceExceptionOnExists() {
        // Arrange
        Long accountId = 1L;
        when(accountRepository.existsById(accountId))
                .thenThrow(new PersistenceException("Database error"));

        // Act & Assert
        assertThatThrownBy(() -> accountService.getAccount(accountId))
                .isInstanceOf(AccountServiceException.class)
                .hasMessageContaining(ErrorInfo.INTERNAL_SERVER_ERROR_WHILE_GETTING_FROM_DB.getErrMsg());

        verify(accountRepository, times(1)).existsById(accountId);
        verify(accountRepository, never()).getReferenceById(any());
    }

    @Test
    @DisplayName("getAccount - should throw AccountServiceException when PersistenceException occurs during getReferenceById")
    void testGetAccount_PersistenceExceptionOnGetReference() {
        // Arrange
        Long accountId = 1L;
        when(accountRepository.existsById(accountId)).thenReturn(true);
        when(accountRepository.getReferenceById(accountId))
                .thenThrow(new PersistenceException("Database error"));

        // Act & Assert
        assertThatThrownBy(() -> accountService.getAccount(accountId))
                .isInstanceOf(AccountServiceException.class)
                .hasMessageContaining(ErrorInfo.INTERNAL_SERVER_ERROR_WHILE_GETTING_FROM_DB.getErrMsg());

        verify(accountRepository, times(1)).existsById(accountId);
        verify(accountRepository, times(1)).getReferenceById(accountId);
    }

    @Test
    @DisplayName("getAccount - should check existence before fetching")
    void testGetAccount_CheckExistenceBeforeFetch() {
        // Arrange
        Long accountId = 1L;
        when(accountRepository.existsById(accountId)).thenReturn(true);
        when(accountRepository.getReferenceById(accountId)).thenReturn(accountEntity);

        // Act
        accountService.getAccount(accountId);

        // Assert
        InOrder inOrder = inOrder(accountRepository);
        inOrder.verify(accountRepository).existsById(accountId);
        inOrder.verify(accountRepository).getReferenceById(accountId);
    }

    @Test
    @DisplayName("getAccount - should not call getReferenceById when account doesn't exist")
    void testGetAccount_NoReferenceCallWhenNotExists() {
        // Arrange
        Long accountId = 999L;
        when(accountRepository.existsById(accountId)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> accountService.getAccount(accountId))
                .isInstanceOf(AccountServiceException.class);

        verify(accountRepository, times(1)).existsById(accountId);
        verify(accountRepository, never()).getReferenceById(any());
    }

    @Test
    @DisplayName("getAccount - should return correct account ID")
    void testGetAccount_ReturnCorrectAccountId() {
        // Arrange
        Long accountId = 42L;
        AccountEntity entity = AccountEntity.builder()
                .accountId(accountId)
                .documentNumber("12345678901")
                .build();
        when(accountRepository.existsById(accountId)).thenReturn(true);
        when(accountRepository.getReferenceById(accountId)).thenReturn(entity);

        // Act
        AccountRes result = accountService.getAccount(accountId);

        // Assert
        assertThat(result.getAccountId()).isEqualTo(accountId);
    }

    @Test
    @DisplayName("getAccount - should return correct document number")
    void testGetAccount_ReturnCorrectDocumentNumber() {
        // Arrange
        Long accountId = 1L;
        String documentNumber = "99887766554";
        AccountEntity entity = AccountEntity.builder()
                .accountId(accountId)
                .documentNumber(documentNumber)
                .build();
        when(accountRepository.existsById(accountId)).thenReturn(true);
        when(accountRepository.getReferenceById(accountId)).thenReturn(entity);

        // Act
        AccountRes result = accountService.getAccount(accountId);

        // Assert
        assertThat(result.getDocumentNumber()).isEqualTo(documentNumber);
    }

    @Test
    @DisplayName("getAccount - should throw exception with correct error info on persistence error")
    void testGetAccount_VerifyExceptionErrorInfo() {
        // Arrange
        Long accountId = 1L;
        when(accountRepository.existsById(accountId))
                .thenThrow(new PersistenceException("DB error"));

        // Act & Assert
        assertThatThrownBy(() -> accountService.getAccount(accountId))
                .isInstanceOf(AccountServiceException.class)
                .extracting("errorInfo")
                .isEqualTo(ErrorInfo.INTERNAL_SERVER_ERROR_WHILE_GETTING_FROM_DB);
    }

    @Test
    @DisplayName("getAccount - should handle multiple consecutive calls")
    void testGetAccount_MultipleCalls() {
        // Arrange
        Long accountId = 1L;
        when(accountRepository.existsById(accountId)).thenReturn(true);
        when(accountRepository.getReferenceById(accountId)).thenReturn(accountEntity);

        // Act
        AccountRes result1 = accountService.getAccount(accountId);
        AccountRes result2 = accountService.getAccount(accountId);

        // Assert
        assertThat(result1).isEqualTo(result2);
        verify(accountRepository, times(2)).existsById(accountId);
        verify(accountRepository, times(2)).getReferenceById(accountId);
    }

    @Test
    @DisplayName("getAccount - should call existsById with correct account ID")
    void testGetAccount_VerifyExistsByIdCalled() {
        // Arrange
        Long accountId = 123L;
        when(accountRepository.existsById(accountId)).thenReturn(true);
        when(accountRepository.getReferenceById(accountId)).thenReturn(accountEntity);

        // Act
        accountService.getAccount(accountId);

        // Assert
        verify(accountRepository, times(1)).existsById(123L);
    }

    @Test
    @DisplayName("getAccount - should call getReferenceById with correct account ID")
    void testGetAccount_VerifyGetReferenceByIdCalled() {
        // Arrange
        Long accountId = 456L;
        when(accountRepository.existsById(accountId)).thenReturn(true);
        when(accountRepository.getReferenceById(accountId)).thenReturn(accountEntity);

        // Act
        accountService.getAccount(accountId);

        // Assert
        verify(accountRepository, times(1)).getReferenceById(456L);
    }
}
