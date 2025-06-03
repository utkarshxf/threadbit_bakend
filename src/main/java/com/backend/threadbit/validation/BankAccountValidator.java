package com.backend.threadbit.validation;

import com.backend.threadbit.dto.BankAccountDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BankAccountValidator implements ConstraintValidator<ValidBankAccount, BankAccountDTO> {

    @Override
    public void initialize(ValidBankAccount constraintAnnotation) {
        // No initialization needed
    }

    @Override
    public boolean isValid(BankAccountDTO bankAccountDTO, ConstraintValidatorContext context) {
        if (bankAccountDTO == null) {
            return true; // Let @NotNull handle null validation
        }

        // Disable default error message
        context.disableDefaultConstraintViolation();

        if (bankAccountDTO.getAccountType() == null) {
            context.buildConstraintViolationWithTemplate("Account type is required")
                    .addPropertyNode("accountType")
                    .addConstraintViolation();
            return false;
        }

        boolean isValid = true;

        switch (bankAccountDTO.getAccountType()) {
            case BANK_ACCOUNT:
                // Validate bank account fields
                if (isEmpty(bankAccountDTO.getAccountNumber())) {
                    context.buildConstraintViolationWithTemplate("Account number is required for bank account")
                            .addPropertyNode("accountNumber")
                            .addConstraintViolation();
                    isValid = false;
                } else if (!bankAccountDTO.getAccountNumber().matches("^[0-9]{9,18}$")) {
                    context.buildConstraintViolationWithTemplate("Account number must be between 9 and 18 digits")
                            .addPropertyNode("accountNumber")
                            .addConstraintViolation();
                    isValid = false;
                }

                if (isEmpty(bankAccountDTO.getAccountHolderName())) {
                    context.buildConstraintViolationWithTemplate("Account holder name is required for bank account")
                            .addPropertyNode("accountHolderName")
                            .addConstraintViolation();
                    isValid = false;
                }

                if (isEmpty(bankAccountDTO.getBankName())) {
                    context.buildConstraintViolationWithTemplate("Bank name is required for bank account")
                            .addPropertyNode("bankName")
                            .addConstraintViolation();
                    isValid = false;
                }

                if (isEmpty(bankAccountDTO.getIfscCode())) {
                    context.buildConstraintViolationWithTemplate("IFSC code is required for bank account")
                            .addPropertyNode("ifscCode")
                            .addConstraintViolation();
                    isValid = false;
                } else if (!bankAccountDTO.getIfscCode().matches("^[A-Z]{4}0[A-Z0-9]{6}$")) {
                    context.buildConstraintViolationWithTemplate("Invalid IFSC code format")
                            .addPropertyNode("ifscCode")
                            .addConstraintViolation();
                    isValid = false;
                }

                // UPI should be empty for bank account
                if (!isEmpty(bankAccountDTO.getUpiId())) {
                    context.buildConstraintViolationWithTemplate("UPI ID should not be provided for bank account")
                            .addPropertyNode("upiId")
                            .addConstraintViolation();
                    isValid = false;
                }
                break;

            case UPI:
                // Validate UPI fields
                if (isEmpty(bankAccountDTO.getUpiId())) {
                    context.buildConstraintViolationWithTemplate("UPI ID is required for UPI account")
                            .addPropertyNode("upiId")
                            .addConstraintViolation();
                    isValid = false;
                }

                // Bank account fields should be empty for UPI
                if (!isEmpty(bankAccountDTO.getAccountNumber()) || 
                    !isEmpty(bankAccountDTO.getAccountHolderName()) || 
                    !isEmpty(bankAccountDTO.getBankName()) || 
                    !isEmpty(bankAccountDTO.getIfscCode())) {
                    context.buildConstraintViolationWithTemplate("Bank account details should not be provided for UPI account")
                            .addConstraintViolation();
                    isValid = false;
                }
                break;
        }

        return isValid;
    }

    private boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}