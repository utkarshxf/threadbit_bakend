package com.backend.threadbit.dto;

import com.backend.threadbit.model.ShippingRecord.ShippingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * DTO for submitting shipping details
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShippingDetailsDto {
    
    // ID of the item that was sold
    @NotBlank(message = "Item ID is required")
    private String itemId;
    
    // ID of the purchase (for instant buy) - optional if it's a bid win
    private String purchaseId;
    
    // ID of the winning bid (for auctions) - optional if it's an instant buy
    private String bidId;
    
    // ID of the seller
    @NotBlank(message = "Seller ID is required")
    private String sellerId;
    
    // ID of the buyer
    @NotBlank(message = "Buyer ID is required")
    private String buyerId;
    
    // Shipping details
    @NotBlank(message = "Tracking number is required")
    private String trackingNumber;
    
    @NotBlank(message = "Carrier is required")
    private String carrier;
    
    @NotBlank(message = "Shipping method is required")
    private String shippingMethod;
    
    private String additionalNotes;
    
    // Receipt image (Base64 encoded string)
    @NotBlank(message = "Receipt image is required")
    private String receiptImageUrl;
    
    // Status
    @NotNull(message = "Shipping status is required")
    private ShippingStatus status;
}