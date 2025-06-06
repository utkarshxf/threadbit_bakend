package com.backend.threadbit.model;

import com.backend.threadbit.dto.BankAccountDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "bank_accounts")
public class BankAccount {

    @Id
    private String id;

    private BankAccountDTO.AccountType accountType;

    private String accountNumber;
    private String accountHolderName;
    private String bankName;
    private String ifscCode;
    private String upiId;

    private String userId;

    @DBRef(lazy = true)
    private User user;

    private boolean isActive;
    private boolean isPrimary;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
}
