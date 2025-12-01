package com.banking.fintech.controller;

import com.banking.fintech.dto.AccountReq;
import com.banking.fintech.dto.AccountRes;
import com.banking.fintech.service.AccountService;
import com.banking.fintech.validator.AccountValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.inOrder;

@ExtendWith(MockitoExtension.class)
@DisplayName("AccountControllerImpl Test Suite")
class AccountControllerImplTest {

    @Mock
    private AccountValidator accountValidator;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountControllerImpl accountController;

    private AccountReq accountReq;
    private AccountRes accountRes;

    @BeforeEach
    void setUp() {
        accountReq = AccountReq.builder()
                .documentNumber("12345678901")
                .build();

        accountRes = AccountRes.builder()
                .accountId(1L)
                .documentNumber("12345678901")
                .build();
    }

    // ============= createAccount Tests =============

    @Test
    @DisplayName("Should successfully create account when valid request is provided")
    void shouldCreateAccountSuccessfully() {
        // Arrange
        doNothing().when(accountValidator).validateCreateAccountReq(accountReq);
        when(accountService.createAccount(accountReq)).thenReturn(accountRes);

        // Act
        ResponseEntity<AccountRes> response = accountController.createAccount(accountReq);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getAccountId()).isEqualTo(1L);
        assertThat(response.getBody().getDocumentNumber()).isEqualTo("12345678901");

        verify(accountValidator, times(1)).validateCreateAccountReq(accountReq);
        verify(accountService, times(1)).createAccount(accountReq);
    }

    @Test
    @DisplayName("Should call validator before creating account")
    void shouldCallValidatorBeforeCreateAccount() {
        // Arrange
        doNothing().when(accountValidator).validateCreateAccountReq(accountReq);
        when(accountService.createAccount(accountReq)).thenReturn(accountRes);

        // Act
        accountController.createAccount(accountReq);

        // Assert
        InOrder inOrder = inOrder(accountValidator, accountService);
        inOrder.verify(accountValidator).validateCreateAccountReq(accountReq);
        inOrder.verify(accountService).createAccount(accountReq);
    }

    @Test
    @DisplayName("Should return HTTP 200 status when account is created successfully")
    void shouldReturnOkStatusWhenAccountCreated() {
        // Arrange
        doNothing().when(accountValidator).validateCreateAccountReq(accountReq);
        when(accountService.createAccount(accountReq)).thenReturn(accountRes);

        // Act
        ResponseEntity<AccountRes> response = accountController.createAccount(accountReq);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("Should pass request object to service during account creation")
    void shouldPassRequestToServiceDuringCreation() {
        // Arrange
        doNothing().when(accountValidator).validateCreateAccountReq(accountReq);
        when(accountService.createAccount(accountReq)).thenReturn(accountRes);

        // Act
        accountController.createAccount(accountReq);

        // Assert
        verify(accountService).createAccount(accountReq);
    }

    @Test
    @DisplayName("Should handle account creation with different document numbers")
    void shouldCreateAccountWithDifferentDocumentNumbers() {
        // Arrange
        AccountReq req1 = AccountReq.builder().documentNumber("11111111111").build();
        AccountReq req2 = AccountReq.builder().documentNumber("22222222222").build();

        AccountRes res1 = AccountRes.builder().accountId(1L).documentNumber("11111111111").build();
        AccountRes res2 = AccountRes.builder().accountId(2L).documentNumber("22222222222").build();

        doNothing().when(accountValidator).validateCreateAccountReq(any());
        when(accountService.createAccount(req1)).thenReturn(res1);
        when(accountService.createAccount(req2)).thenReturn(res2);

        // Act
        ResponseEntity<AccountRes> response1 = accountController.createAccount(req1);
        ResponseEntity<AccountRes> response2 = accountController.createAccount(req2);

        // Assert
        assertThat(response1.getBody().getAccountId()).isEqualTo(1L);
        assertThat(response2.getBody().getAccountId()).isEqualTo(2L);
        verify(accountService).createAccount(req1);
        verify(accountService).createAccount(req2);
    }

    @Test
    @DisplayName("Should return response body with all required fields after account creation")
    void shouldReturnCompleteResponseAfterCreation() {
        // Arrange
        doNothing().when(accountValidator).validateCreateAccountReq(accountReq);
        when(accountService.createAccount(accountReq)).thenReturn(accountRes);

        // Act
        ResponseEntity<AccountRes> response = accountController.createAccount(accountReq);
        AccountRes body = response.getBody();

        // Assert
        assertThat(body)
                .isNotNull()
                .hasFieldOrPropertyWithValue("accountId", 1L)
                .hasFieldOrPropertyWithValue("documentNumber", "12345678901");
    }

    // ============= getAccount Tests =============

    @Test
    @DisplayName("Should successfully retrieve account by account ID")
    void shouldGetAccountSuccessfully() {
        // Arrange
        Long accountId = 1L;
        when(accountService.getAccount(accountId)).thenReturn(accountRes);

        // Act
        ResponseEntity<AccountRes> response = accountController.getAccount(accountId);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getAccountId()).isEqualTo(1L);
        assertThat(response.getBody().getDocumentNumber()).isEqualTo("12345678901");

        verify(accountService, times(1)).getAccount(accountId);
    }

    @Test
    @DisplayName("Should return HTTP 200 status when account is retrieved")
    void shouldReturnOkStatusWhenAccountRetrieved() {
        // Arrange
        Long accountId = 1L;
        when(accountService.getAccount(accountId)).thenReturn(accountRes);

        // Act
        ResponseEntity<AccountRes> response = accountController.getAccount(accountId);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("Should pass account ID to service during retrieval")
    void shouldPassAccountIdToService() {
        // Arrange
        Long accountId = 5L;
        when(accountService.getAccount(accountId)).thenReturn(accountRes);

        // Act
        accountController.getAccount(accountId);

        // Assert
        verify(accountService).getAccount(accountId);
    }

    @Test
    @DisplayName("Should retrieve account with correct account ID")
    void shouldRetrieveAccountWithCorrectId() {
        // Arrange
        Long expectedAccountId = 123L;
        AccountRes expectedRes = AccountRes.builder()
                .accountId(expectedAccountId)
                .documentNumber("98765432101")
                .build();
        when(accountService.getAccount(expectedAccountId)).thenReturn(expectedRes);

        // Act
        ResponseEntity<AccountRes> response = accountController.getAccount(expectedAccountId);

        // Assert
        assertThat(response.getBody().getAccountId()).isEqualTo(expectedAccountId);
        verify(accountService).getAccount(expectedAccountId);
    }

    @Test
    @DisplayName("Should handle retrieval of different accounts by different IDs")
    void shouldRetrieveDifferentAccountsByDifferentIds() {
        // Arrange
        AccountRes res1 = AccountRes.builder().accountId(1L).documentNumber("11111111111").build();
        AccountRes res2 = AccountRes.builder().accountId(2L).documentNumber("22222222222").build();

        when(accountService.getAccount(1L)).thenReturn(res1);
        when(accountService.getAccount(2L)).thenReturn(res2);

        // Act
        ResponseEntity<AccountRes> response1 = accountController.getAccount(1L);
        ResponseEntity<AccountRes> response2 = accountController.getAccount(2L);

        // Assert
        assertThat(response1.getBody().getAccountId()).isEqualTo(1L);
        assertThat(response2.getBody().getAccountId()).isEqualTo(2L);
        verify(accountService).getAccount(1L);
        verify(accountService).getAccount(2L);
    }

    @Test
    @DisplayName("Should return response body with all required fields during retrieval")
    void shouldReturnCompleteResponseDuringRetrieval() {
        // Arrange
        Long accountId = 1L;
        when(accountService.getAccount(accountId)).thenReturn(accountRes);

        // Act
        ResponseEntity<AccountRes> response = accountController.getAccount(accountId);
        AccountRes body = response.getBody();

        // Assert
        assertThat(body)
                .isNotNull()
                .hasFieldOrPropertyWithValue("accountId", 1L)
                .hasFieldOrPropertyWithValue("documentNumber", "12345678901");
    }

    // ============= Dependency Injection Tests =============

    @Test
    @DisplayName("Should properly inject AccountValidator dependency")
    void shouldInjectAccountValidatorDependency() {
        // Assert
        assertThat(accountController).isNotNull();
        assertThat(accountValidator).isNotNull();
    }

    @Test
    @DisplayName("Should properly inject AccountService dependency")
    void shouldInjectAccountServiceDependency() {
        // Assert
        assertThat(accountController).isNotNull();
        assertThat(accountService).isNotNull();
    }

    // ============= Edge Cases and Error Handling =============

    @Test
    @DisplayName("Should handle null response from service gracefully")
    void shouldHandleNullResponseFromService() {
        // Arrange
        doNothing().when(accountValidator).validateCreateAccountReq(accountReq);
        when(accountService.createAccount(accountReq)).thenReturn(null);

        // Act
        ResponseEntity<AccountRes> response = accountController.createAccount(accountReq);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNull();
    }

    @Test
    @DisplayName("Should handle empty document number gracefully")
    void shouldHandleEmptyDocumentNumber() {
        // Arrange
        AccountReq emptyReq = AccountReq.builder().documentNumber("").build();
        doNothing().when(accountValidator).validateCreateAccountReq(emptyReq);
        when(accountService.createAccount(emptyReq)).thenReturn(accountRes);

        // Act
        ResponseEntity<AccountRes> response = accountController.createAccount(emptyReq);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(accountValidator).validateCreateAccountReq(emptyReq);
    }

    @Test
    @DisplayName("Should handle account retrieval with zero account ID")
    void shouldHandleAccountRetrievalWithZeroId() {
        // Arrange
        Long zeroId = 0L;
        when(accountService.getAccount(zeroId)).thenReturn(accountRes);

        // Act
        ResponseEntity<AccountRes> response = accountController.getAccount(zeroId);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(accountService).getAccount(zeroId);
    }

    @Test
    @DisplayName("Should handle account retrieval with large account ID")
    void shouldHandleAccountRetrievalWithLargeId() {
        // Arrange
        Long largeId = Long.MAX_VALUE;
        when(accountService.getAccount(largeId)).thenReturn(accountRes);

        // Act
        ResponseEntity<AccountRes> response = accountController.getAccount(largeId);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(accountService).getAccount(largeId);
    }

    // ============= Verification Tests =============

    @Test
    @DisplayName("Should not call validator when getting account")
    void shouldNotCallValidatorWhenGettingAccount() {
        // Arrange
        when(accountService.getAccount(1L)).thenReturn(accountRes);

        // Act
        accountController.getAccount(1L);

        // Assert
        verify(accountValidator, never()).validateCreateAccountReq(any());
    }

    @Test
    @DisplayName("Should call service exactly once during account creation")
    void shouldCallServiceExactlyOnce() {
        // Arrange
        doNothing().when(accountValidator).validateCreateAccountReq(accountReq);
        when(accountService.createAccount(accountReq)).thenReturn(accountRes);

        // Act
        accountController.createAccount(accountReq);

        // Assert
        verify(accountService, times(1)).createAccount(accountReq);
    }

    @Test
    @DisplayName("Should call service exactly once during account retrieval")
    void shouldCallServiceExactlyOnceForRetrieval() {
        // Arrange
        when(accountService.getAccount(1L)).thenReturn(accountRes);

        // Act
        accountController.getAccount(1L);

        // Assert
        verify(accountService, times(1)).getAccount(1L);
    }

}
