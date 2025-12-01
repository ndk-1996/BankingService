package com.banking.fintech.validator;

import com.banking.fintech.constant.ErrorInfo;
import com.banking.fintech.dto.AccountReq;
import com.banking.fintech.exception.AccountServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

@DisplayName("AccountValidator Test Suite")
class AccountValidatorTest {

    private AccountValidator accountValidator;

    @BeforeEach
    void setUp() {
        accountValidator = new AccountValidator();
    }

    @Test
    @DisplayName("Should successfully validate account request with valid numeric document number")
    void shouldValidateWithValidNumericDocumentNumber() {
        // Arrange
        AccountReq accountReq = AccountReq.builder()
                .documentNumber("12345678901")
                .build();

        // Act & Assert - should not throw an exception
        assertThatNoException()
                .isThrownBy(() -> accountValidator.validateCreateAccountReq(accountReq));
    }

    @Test
    @DisplayName("Should successfully validate account request with single digit document number")
    void shouldValidateWithSingleDigitDocumentNumber() {
        // Arrange
        AccountReq accountReq = AccountReq.builder()
                .documentNumber("1")
                .build();

        // Act & Assert
        assertThatNoException()
                .isThrownBy(() -> accountValidator.validateCreateAccountReq(accountReq));
    }

    @Test
    @DisplayName("Should successfully validate account request with long numeric document number")
    void shouldValidateWithLongNumericDocumentNumber() {
        // Arrange
        AccountReq accountReq = AccountReq.builder()
                .documentNumber("123456789012345678901234567890")
                .build();

        // Act & Assert
        assertThatNoException()
                .isThrownBy(() -> accountValidator.validateCreateAccountReq(accountReq));
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "999", "1234567890", "00000", "999999999999"})
    @DisplayName("Should validate various valid numeric document numbers")
    void shouldValidateMultipleValidDocumentNumbers(String documentNumber) {
        // Arrange
        AccountReq accountReq = AccountReq.builder()
                .documentNumber(documentNumber)
                .build();

        // Act & Assert
        assertThatNoException()
                .isThrownBy(() -> accountValidator.validateCreateAccountReq(accountReq));
    }

    @Test
    @DisplayName("Should throw exception for document number with alphabetic characters")
    void shouldThrowExceptionForAlphabeticCharacters() {
        // Arrange
        AccountReq accountReq = AccountReq.builder()
                .documentNumber("123ABC456")
                .build();

        // Act & Assert
        assertThatThrownBy(() -> accountValidator.validateCreateAccountReq(accountReq))
                .isInstanceOf(AccountServiceException.class)
                .hasFieldOrPropertyWithValue("errorInfo", ErrorInfo.DOCUMENT_NUMBER_SHOULD_ONLY_CONSIST_OF_DIGITS);
    }

    @Test
    @DisplayName("Should throw exception for document number with special characters")
    void shouldThrowExceptionForSpecialCharacters() {
        // Arrange
        AccountReq accountReq = AccountReq.builder()
                .documentNumber("123-456-789")
                .build();

        // Act & Assert
        assertThatThrownBy(() -> accountValidator.validateCreateAccountReq(accountReq))
                .isInstanceOf(AccountServiceException.class)
                .hasFieldOrPropertyWithValue("errorInfo", ErrorInfo.DOCUMENT_NUMBER_SHOULD_ONLY_CONSIST_OF_DIGITS);
    }

    @Test
    @DisplayName("Should throw exception for document number with spaces")
    void shouldThrowExceptionForSpaces() {
        // Arrange
        AccountReq accountReq = AccountReq.builder()
                .documentNumber("123 456 789")
                .build();

        // Act & Assert
        assertThatThrownBy(() -> accountValidator.validateCreateAccountReq(accountReq))
                .isInstanceOf(AccountServiceException.class)
                .hasFieldOrPropertyWithValue("errorInfo", ErrorInfo.DOCUMENT_NUMBER_SHOULD_ONLY_CONSIST_OF_DIGITS);
    }

    @Test
    @DisplayName("Should throw exception for document number with decimal point")
    void shouldThrowExceptionForDecimalPoint() {
        // Arrange
        AccountReq accountReq = AccountReq.builder()
                .documentNumber("123.456")
                .build();

        // Act & Assert
        assertThatThrownBy(() -> accountValidator.validateCreateAccountReq(accountReq))
                .isInstanceOf(AccountServiceException.class)
                .hasFieldOrPropertyWithValue("errorInfo", ErrorInfo.DOCUMENT_NUMBER_SHOULD_ONLY_CONSIST_OF_DIGITS);
    }

    @Test
    @DisplayName("Should throw exception for document number with comma")
    void shouldThrowExceptionForComma() {
        // Arrange
        AccountReq accountReq = AccountReq.builder()
                .documentNumber("123,456")
                .build();

        // Act & Assert
        assertThatThrownBy(() -> accountValidator.validateCreateAccountReq(accountReq))
                .isInstanceOf(AccountServiceException.class)
                .hasFieldOrPropertyWithValue("errorInfo", ErrorInfo.DOCUMENT_NUMBER_SHOULD_ONLY_CONSIST_OF_DIGITS);
    }

    @Test
    @DisplayName("Should throw exception for document number with @ symbol")
    void shouldThrowExceptionForAtSymbol() {
        // Arrange
        AccountReq accountReq = AccountReq.builder()
                .documentNumber("123@456")
                .build();

        // Act & Assert
        assertThatThrownBy(() -> accountValidator.validateCreateAccountReq(accountReq))
                .isInstanceOf(AccountServiceException.class)
                .hasFieldOrPropertyWithValue("errorInfo", ErrorInfo.DOCUMENT_NUMBER_SHOULD_ONLY_CONSIST_OF_DIGITS);
    }

    @ParameterizedTest
    @ValueSource(strings = {"12A", "AB12", "1@2", "1.2", "1,2", "abc", "!@#", "12 34", "12-34"})
    @DisplayName("Should throw exception for various invalid document numbers")
    void shouldThrowExceptionForVariousInvalidDocumentNumbers(String documentNumber) {
        // Arrange
        AccountReq accountReq = AccountReq.builder()
                .documentNumber(documentNumber)
                .build();

        // Act & Assert
        assertThatThrownBy(() -> accountValidator.validateCreateAccountReq(accountReq))
                .isInstanceOf(AccountServiceException.class)
                .hasFieldOrPropertyWithValue("errorInfo", ErrorInfo.DOCUMENT_NUMBER_SHOULD_ONLY_CONSIST_OF_DIGITS);
    }
}
