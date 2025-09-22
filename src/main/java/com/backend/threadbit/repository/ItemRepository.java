package com.backend.threadbit.repository;

import com.backend.threadbit.model.Item;
import com.backend.threadbit.model.Size;
import com.backend.threadbit.model.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface ItemRepository extends MongoRepository<Item, String> {
    List<Item> findByCategoryIdOrderByCreatedAtDesc(Integer categoryId);
    List<Item> findBySellerIdOrderByCreatedAtDesc(String sellerId);

    // Pagination methods
    Page<Item> findAll(Pageable pageable);
    Page<Item> findByCategoryId(Integer categoryId, Pageable pageable);
    Page<Item> findBySellerId(String sellerId, Pageable pageable);
    Page<Item> findByStatus(Status status, Pageable pageable);
    Page<Item> findBySize(Size size, Pageable pageable);

    // Search methods
    @Query("{ $or: [ { 'title': { $regex: ?0, $options: 'i' } }, { 'description': { $regex: ?0, $options: 'i' } } ] }")
    Page<Item> searchByTitleOrDescription(String keyword, Pageable pageable);

    // Combined search and filter
    @Query("{ $and: [ " +
           "{ $or: [ { 'title': { $regex: ?0, $options: 'i' } }, { 'description': { $regex: ?0, $options: 'i' } } ] }, " +
           "{ 'categoryId': ?1 } ] }")
    Page<Item> searchByTitleOrDescriptionAndCategory(String keyword, Integer categoryId, Pageable pageable);

    @Query("{ $and: [ " +
           "{ $or: [ { 'title': { $regex: ?0, $options: 'i' } }, { 'description': { $regex: ?0, $options: 'i' } } ] }, " +
           "{ 'status': ?1 } ] }")
    Page<Item> searchByTitleOrDescriptionAndStatus(String keyword, Status status, Pageable pageable);

    @Query("{ $and: [ " +
           "{ $or: [ { 'title': { $regex: ?0, $options: 'i' } }, { 'description': { $regex: ?0, $options: 'i' } } ] }, " +
           "{ 'size': ?1 } ] }")
    Page<Item> searchByTitleOrDescriptionAndSize(String keyword, Size size, Pageable pageable);

    // Find by seller username (requires join with User collection)
    @Query("{ 'sellerId': { $in: ?0 } }")
    Page<Item> findBySellerIds(List<String> sellerIds, Pageable pageable);


    // Get items where user is bidding (auction not ended)

    @Query(value = "{ 'endTime': { '$gt': ?1 }, '_id': { '$in': ?0 } }", sort = "{ 'createdAt': -1 }")
    List<Item> getActiveBidItemsByUserId(List<String> itemIds, ZonedDateTime currentTime);

    // Get items where user won (auction ended and user has highest bid)
    @Query(value = "{ 'endTime': { '$lt': ?1 }, '_id': { '$in': ?0 } }", sort = "{ 'createdAt': -1 }")
    List<Item> getWonItemsByUserId(List<String> itemIds, ZonedDateTime currentTime);

    // Get items where user lost (auction ended and user doesn't have highest bid)
    @Query(value = "{ 'endTime': { '$lt': ?1 }, '_id': { '$in': ?0 } }", sort = "{ 'createdAt': -1 }")
    List<Item> getLostItemsByUserId(List<String> itemIds, ZonedDateTime currentTime);

    // Alternative: Get all items by IDs (for processing in service layer)
    @Query(value = "{ '_id': { '$in': ?0 } }", sort = "{ 'createdAt': -1 }")
    List<Item> findItemsByIds(List<String> itemIds);

    @Query("{ $and: [ " +
            "{ 'itemType': 'INSTANT_BUY' }, " +
            "{ 'status': 'ACTIVE' }, " +
            "{ 'stockQuantity': { $gt: 0 } }, " +
            "{ $or: [ { 'title': { $regex: ?0, $options: 'i' } }, { 'description': { $regex: ?0, $options: 'i' } } ] } ] }")
    Page<Item> searchAvailableInstantBuyItems(String keyword, Pageable pageable);

    // Find active auctions that have ended but not yet processed
    @Query("{ $and: [ " +
            "{ 'itemType': 'AUCTION' }, " +
            "{ 'status': 'ACTIVE' }, " +
            "{ 'endTime': { $lt: ?0 } } ] }")
    List<Item> findEndedActiveAuctions(ZonedDateTime currentTime);
}
