package com.backend.threadbit.service;

import com.backend.threadbit.dto.ShippingDetailsDto;
import com.backend.threadbit.model.ShippingRecord;

import java.util.List;

/**
 * Service for managing shipping records
 */
public interface ShippingService {
    
    /**
     * Add shipping details for a sold item
     * 
     * @param shippingDetailsDto the shipping details
     * @return the created shipping record
     */
    ShippingRecord addShippingDetails(ShippingDetailsDto shippingDetailsDto);
    
    /**
     * Get shipping record by ID
     * 
     * @param id the ID of the shipping record
     * @return the shipping record
     */
    ShippingRecord getShippingRecordById(String id);
    
    /**
     * Get shipping records by item ID
     * 
     * @param itemId the ID of the item
     * @return list of shipping records
     */
    List<ShippingRecord> getShippingRecordsByItemId(String itemId);
    
    /**
     * Get shipping records by purchase ID
     * 
     * @param purchaseId the ID of the purchase
     * @return list of shipping records
     */
    List<ShippingRecord> getShippingRecordsByPurchaseId(String purchaseId);
    
    /**
     * Get shipping records by bid ID
     * 
     * @param bidId the ID of the bid
     * @return list of shipping records
     */
    List<ShippingRecord> getShippingRecordsByBidId(String bidId);
    
    /**
     * Get shipping records by seller ID
     * 
     * @param sellerId the ID of the seller
     * @return list of shipping records
     */
    List<ShippingRecord> getShippingRecordsBySellerId(String sellerId);
    
    /**
     * Get shipping records by buyer ID
     * 
     * @param buyerId the ID of the buyer
     * @return list of shipping records
     */
    List<ShippingRecord> getShippingRecordsByBuyerId(String buyerId);
    
    /**
     * Update shipping record status
     * 
     * @param id the ID of the shipping record
     * @param status the new status
     * @return the updated shipping record
     */
    ShippingRecord updateShippingRecordStatus(String id, ShippingRecord.ShippingStatus status);
}