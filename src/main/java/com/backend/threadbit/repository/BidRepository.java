package com.backend.threadbit.repository;


import com.backend.threadbit.model.Bid;
import com.backend.threadbit.model.BidGroupResult;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidRepository extends MongoRepository<Bid, String> {
    List<Bid> findByItemId(String itemId);
    List<Bid> findByUserId(String userId);


    // Get user's highest bid for each item
    @Aggregation(pipeline = {
            "{ '$match': { 'userId': ?0 } }",
            "{ '$sort': { 'amount': -1 } }",
            "{ '$group': { '_id': '$itemId', 'highestBid': { '$first': '$$ROOT' } } }",
            "{ '$replaceRoot': { 'newRoot': '$highestBid' } }"
    })
    List<Bid> getUserHighestBidsPerItem(String userId);

    // Get all bids for specific items (to determine winners)
    @Aggregation(pipeline = {
            "{ '$match': { 'itemId': { '$in': ?0 } } }",
            "{ '$sort': { 'itemId': 1, 'amount': -1 } }",
            "{ '$group': { '_id': '$itemId', 'highestBid': { '$first': '$$ROOT' }, 'allBids': { '$push': '$$ROOT' } } }"
    })
    List<BidGroupResult> getBidsGroupedByItem(List<String> itemIds);

    // Get user's bid rank for specific items
    @Query("{ 'itemId': { '$in': ?0 } }")
    List<Bid> findBidsByItemIds(List<String> itemIds);

    // Find user's bids for specific items
    @Query("{ 'userId': ?0, 'itemId': { '$in': ?1 } }")
    List<Bid> findUserBidsForItems(String userId, List<String> itemIds);

}