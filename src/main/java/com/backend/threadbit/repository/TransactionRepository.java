package com.backend.threadbit.repository;

import com.backend.threadbit.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {
    
    List<Transaction> findByUserId(String userId);
    
    Page<Transaction> findByUserId(String userId, Pageable pageable);
    
    List<Transaction> findByUserIdAndType(String userId, Transaction.TransactionType type);
    
    Page<Transaction> findByUserIdAndType(String userId, Transaction.TransactionType type, Pageable pageable);
    
    List<Transaction> findByUserIdAndStatus(String userId, Transaction.TransactionStatus status);
    
    Page<Transaction> findByUserIdAndStatus(String userId, Transaction.TransactionStatus status, Pageable pageable);
    
    List<Transaction> findByUserIdAndCreatedAtBetween(String userId, LocalDateTime startDate, LocalDateTime endDate);
    
    Page<Transaction> findByUserIdAndCreatedAtBetween(String userId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    Optional<Transaction> findByRazorpayPaymentId(String razorpayPaymentId);
}