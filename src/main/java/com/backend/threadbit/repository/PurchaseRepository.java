package com.backend.threadbit.repository;

import com.backend.threadbit.model.Purchase;
import com.backend.threadbit.model.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseRepository extends MongoRepository<Purchase, String> {
    // Find purchases by buyer ID
    List<Purchase> findByBuyerId(String buyerId);
    
    // Find purchases by item ID
    List<Purchase> findByItemId(String itemId);
    
    // Find purchases by buyer ID and status
    List<Purchase> findByBuyerIdAndStatus(String buyerId, Status status);
    
    // Pagination methods
    Page<Purchase> findByBuyerId(String buyerId, Pageable pageable);
    Page<Purchase> findByItemId(String itemId, Pageable pageable);
    Page<Purchase> findByBuyerIdAndStatus(String buyerId, Status status, Pageable pageable);
}