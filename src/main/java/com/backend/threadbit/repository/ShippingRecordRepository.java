package com.backend.threadbit.repository;

import com.backend.threadbit.model.ShippingRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for ShippingRecord entities
 */
@Repository
public interface ShippingRecordRepository extends MongoRepository<ShippingRecord, String> {

    /**
     * Find shipping records by item ID
     * 
     * @param itemId the ID of the item
     * @return list of shipping records ordered by createdAt descending
     */
    List<ShippingRecord> findByItemIdOrderByCreatedAtDesc(String itemId);

    /**
     * Find shipping records by purchase ID
     * 
     * @param purchaseId the ID of the purchase
     * @return list of shipping records ordered by createdAt descending
     */
    List<ShippingRecord> findByPurchaseIdOrderByCreatedAtDesc(String purchaseId);

    /**
     * Find shipping records by bid ID
     * 
     * @param bidId the ID of the bid
     * @return list of shipping records ordered by createdAt descending
     */
    List<ShippingRecord> findByBidIdOrderByCreatedAtDesc(String bidId);

    /**
     * Find shipping records by seller ID
     * 
     * @param sellerId the ID of the seller
     * @return list of shipping records ordered by createdAt descending
     */
    List<ShippingRecord> findBySellerIdOrderByCreatedAtDesc(String sellerId);

    /**
     * Find shipping records by buyer ID
     * 
     * @param buyerId the ID of the buyer
     * @return list of shipping records ordered by createdAt descending
     */
    List<ShippingRecord> findByBuyerIdOrderByCreatedAtDesc(String buyerId);

    /**
     * Find shipping record by item ID and seller ID
     * 
     * @param itemId the ID of the item
     * @param sellerId the ID of the seller
     * @return optional shipping record
     */
    Optional<ShippingRecord> findByItemIdAndSellerId(String itemId, String sellerId);

    /**
     * Find shipping record by item ID and buyer ID
     * 
     * @param itemId the ID of the item
     * @param buyerId the ID of the buyer
     * @return optional shipping record
     */
    Optional<ShippingRecord> findByItemIdAndBuyerId(String itemId, String buyerId);
}
