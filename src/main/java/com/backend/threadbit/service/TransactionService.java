package com.backend.threadbit.service;

import com.backend.threadbit.dto.CreditRequestDTO;
import com.backend.threadbit.dto.DebitRequestDTO;
import com.backend.threadbit.dto.TransactionDTO;
import com.backend.threadbit.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {
    
    TransactionDTO creditAmount(CreditRequestDTO creditRequestDTO);
    
    TransactionDTO debitAmount(DebitRequestDTO debitRequestDTO);
    
    TransactionDTO getTransactionById(String id, String userId);
    
    List<TransactionDTO> getAllTransactionsByUserId(String userId);
    
    Page<TransactionDTO> getAllTransactionsByUserId(String userId, Pageable pageable);
    
    List<TransactionDTO> getTransactionsByUserIdAndType(String userId, Transaction.TransactionType type);
    
    Page<TransactionDTO> getTransactionsByUserIdAndType(String userId, Transaction.TransactionType type, Pageable pageable);
    
    List<TransactionDTO> getTransactionsByUserIdAndDateRange(String userId, LocalDateTime startDate, LocalDateTime endDate);
    
    Page<TransactionDTO> getTransactionsByUserIdAndDateRange(String userId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}