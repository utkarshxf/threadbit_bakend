package com.backend.threadbit.repository;

import com.backend.threadbit.model.Item;
import com.backend.threadbit.model.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends MongoRepository<Item, String> {
    List<Item> findByCategoryId(String categoryId);
    List<Item> findBySellerId(String sellerId);

    // Pagination methods
    Page<Item> findAll(Pageable pageable);
    Page<Item> findByCategoryId(String categoryId, Pageable pageable);
    Page<Item> findBySellerId(String sellerId, Pageable pageable);
    Page<Item> findByStatus(Status status, Pageable pageable);

    // Search methods
    @Query("{ $or: [ { 'title': { $regex: ?0, $options: 'i' } }, { 'description': { $regex: ?0, $options: 'i' } } ] }")
    Page<Item> searchByTitleOrDescription(String keyword, Pageable pageable);

    // Combined search and filter
    @Query("{ $and: [ " +
           "{ $or: [ { 'title': { $regex: ?0, $options: 'i' } }, { 'description': { $regex: ?0, $options: 'i' } } ] }, " +
           "{ 'categoryId': ?1 } ] }")
    Page<Item> searchByTitleOrDescriptionAndCategory(String keyword, String categoryId, Pageable pageable);

    @Query("{ $and: [ " +
           "{ $or: [ { 'title': { $regex: ?0, $options: 'i' } }, { 'description': { $regex: ?0, $options: 'i' } } ] }, " +
           "{ 'status': ?1 } ] }")
    Page<Item> searchByTitleOrDescriptionAndStatus(String keyword, Status status, Pageable pageable);

    // Find by seller username (requires join with User collection)
    @Query("{ 'sellerId': { $in: ?0 } }")
    Page<Item> findBySellerIds(List<String> sellerIds, Pageable pageable);
}
