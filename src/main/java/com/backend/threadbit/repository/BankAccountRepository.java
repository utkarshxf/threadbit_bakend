package com.backend.threadbit.repository;

import com.backend.threadbit.model.BankAccount;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankAccountRepository extends MongoRepository<BankAccount, String> {
    
    List<BankAccount> findByUserId(String userId);
    
    Optional<BankAccount> findByUserIdAndIsPrimaryTrue(String userId);
    
    Optional<BankAccount> findByUserIdAndId(String userId, String id);
    
    List<BankAccount> findByUserIdAndIsActiveTrue(String userId);
    
    boolean existsByUserIdAndAccountNumber(String userId, String accountNumber);
}