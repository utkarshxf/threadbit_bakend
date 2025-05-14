package com.backend.threadbit.repository;


import com.backend.threadbit.model.Bid;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidRepository extends MongoRepository<Bid, String> {
    List<Bid> findByItemId(String itemId);
    List<Bid> findByUserId(String userId);
}