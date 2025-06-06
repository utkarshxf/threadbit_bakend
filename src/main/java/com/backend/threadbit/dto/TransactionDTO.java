package com.backend.threadbit.dto;

import com.backend.threadbit.model.Transaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDTO {
    
    private String id;
    private String userId;
    private String bankAccountId;
    private String bankAccountNumber;
    private String bankName;
    private Transaction.TransactionType type;
    private BigDecimal amount;
    private String currency;
    private Transaction.TransactionStatus status;
    private String referenceId;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}