package com.backend.threadbit.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Model for storing shipping details for sold items
 * This can be for items sold via instant buy or auction win
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "shipping_records")
public class ShippingRecord {

    @Id
    private String id;
    
    // Reference to the item
    private String itemId;
    @DBRef(lazy = true)
    private Item item;
    
    // Reference to the purchase (for instant buy)
    private String purchaseId;
    @DBRef(lazy = true)
    private Purchase purchase;
    
    // Reference to the winning bid (for auctions)
    private String bidId;
    @DBRef(lazy = true)
    private Bid bid;
    
    // Seller and buyer information
    private String sellerId;
    @DBRef(lazy = true)
    private User seller;
    
    private String buyerId;
    @DBRef(lazy = true)
    private User buyer;
    
    // Shipping details
    private String trackingNumber;
    private String carrier;
    private String shippingMethod;
    private String additionalNotes;
    
    // Receipt image URL
    private String receiptImageUrl;
    
    // Timestamps
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;
    
    // Status
    @Builder.Default
    private ShippingStatus status = ShippingStatus.PENDING;
    
    // Enum for shipping status
    public enum ShippingStatus {
        PENDING,
        SHIPPED,
        DELIVERED,
        CANCELLED
    }
}