package com.backend.threadbit.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "transactions")
public class Transaction {

    @Id
    private String id;
    
    private String userId;
    
    @DBRef(lazy = true)
    private User user;
    
    private String bankAccountId;
    
    @DBRef(lazy = true)
    private BankAccount bankAccount;
    
    private TransactionType type;
    
    private BigDecimal amount;
    
    private String currency;
    
    private TransactionStatus status;
    
    private String referenceId;
    
    private String description;
    
    private String razorpayPaymentId;
    
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    public enum TransactionType {
        CREDIT, DEBIT
    }
    
    public enum TransactionStatus {
        PENDING, COMPLETED, FAILED, REFUNDED
    }
}