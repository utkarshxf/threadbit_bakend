package com.backend.threadbit.dto;

import com.backend.threadbit.validation.ValidBankAccount;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ValidBankAccount
public class BankAccountDTO {

    private String id;

    @NotNull(message = "Account type is required")
    private AccountType accountType;

    // Bank account details - required only if accountType is BANK_ACCOUNT
    private String accountNumber;

    private String accountHolderName;

    private String bankName;

    private String ifscCode;

    // UPI details - required only if accountType is UPI
    private String upiId;

    private boolean isActive;

    private boolean isPrimary;

    public enum AccountType {
        BANK_ACCOUNT,
        UPI
    }
}
