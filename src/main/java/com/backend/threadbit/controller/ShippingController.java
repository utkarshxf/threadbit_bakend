package com.backend.threadbit.controller;

import com.backend.threadbit.dto.ShippingDetailsDto;
import com.backend.threadbit.model.ShippingRecord;
import com.backend.threadbit.service.ShippingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Controller for managing shipping records
 */
@RestController
@RequestMapping("/api/shipping")
@CrossOrigin(
        origins = {"https://threadbitwebsite-fqj4l.ondigitalocean.app" , "https://threadbit.in" , "threadbit.in","www.threadbit.in", "https://threadbid.in" , "threadbid.in","www.threadbid.in","http://192.168.32.1:5173", "https://secondhand-threads.vercel.app","http://10.244.72.46:8080"},
        allowCredentials = "true",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
        allowedHeaders = {"Content-Type", "Authorization"}
)
@Slf4j
@RequiredArgsConstructor
public class ShippingController {

    @Autowired
    private final ShippingService shippingService;

    /**
     * Add shipping details for a sold item
     * 
     * @param shippingDetailsDto the shipping details
     * @return the created shipping record
     */
    @PostMapping
    public ResponseEntity<?> addShippingDetails(@Valid @RequestBody ShippingDetailsDto shippingDetailsDto) {
        try {
            ShippingRecord shippingRecord = shippingService.addShippingDetails(shippingDetailsDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(shippingRecord);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("Error adding shipping details", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal server error"));
        }
    }

    /**
     * Get shipping record by ID
     * 
     * @param id the ID of the shipping record
     * @return the shipping record
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getShippingRecordById(@PathVariable String id) {
        try {
            ShippingRecord shippingRecord = shippingService.getShippingRecordById(id);
            return ResponseEntity.ok(shippingRecord);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("Error getting shipping record", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal server error"));
        }
    }

    /**
     * Get shipping records by item ID
     * 
     * @param itemId the ID of the item
     * @return list of shipping records
     */
    @GetMapping("/item/{itemId}")
    public ResponseEntity<?> getShippingRecordsByItemId(@PathVariable String itemId) {
        try {
            List<ShippingRecord> shippingRecords = shippingService.getShippingRecordsByItemId(itemId);
            return ResponseEntity.ok(shippingRecords);
        } catch (Exception e) {
            log.error("Error getting shipping records by item ID", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal server error"));
        }
    }

    /**
     * Get shipping records by purchase ID
     * 
     * @param purchaseId the ID of the purchase
     * @return list of shipping records
     */
    @GetMapping("/purchase/{purchaseId}")
    public ResponseEntity<?> getShippingRecordsByPurchaseId(@PathVariable String purchaseId) {
        try {
            List<ShippingRecord> shippingRecords = shippingService.getShippingRecordsByPurchaseId(purchaseId);
            return ResponseEntity.ok(shippingRecords);
        } catch (Exception e) {
            log.error("Error getting shipping records by purchase ID", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal server error"));
        }
    }

    /**
     * Get shipping records by bid ID
     * 
     * @param bidId the ID of the bid
     * @return list of shipping records
     */
    @GetMapping("/bid/{bidId}")
    public ResponseEntity<?> getShippingRecordsByBidId(@PathVariable String bidId) {
        try {
            List<ShippingRecord> shippingRecords = shippingService.getShippingRecordsByBidId(bidId);
            return ResponseEntity.ok(shippingRecords);
        } catch (Exception e) {
            log.error("Error getting shipping records by bid ID", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal server error"));
        }
    }

    /**
     * Get shipping records by seller ID
     * 
     * @param sellerId the ID of the seller
     * @return list of shipping records
     */
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<?> getShippingRecordsBySellerId(@PathVariable String sellerId) {
        try {
            List<ShippingRecord> shippingRecords = shippingService.getShippingRecordsBySellerId(sellerId);
            return ResponseEntity.ok(shippingRecords);
        } catch (Exception e) {
            log.error("Error getting shipping records by seller ID", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal server error"));
        }
    }

    /**
     * Get shipping records by buyer ID
     * 
     * @param buyerId the ID of the buyer
     * @return list of shipping records
     */
    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<?> getShippingRecordsByBuyerId(@PathVariable String buyerId) {
        try {
            List<ShippingRecord> shippingRecords = shippingService.getShippingRecordsByBuyerId(buyerId);
            return ResponseEntity.ok(shippingRecords);
        } catch (Exception e) {
            log.error("Error getting shipping records by buyer ID", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal server error"));
        }
    }

    /**
     * Update shipping record status
     * 
     * @param id the ID of the shipping record
     * @param status the new status
     * @return the updated shipping record
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateShippingRecordStatus(
            @PathVariable String id,
            @RequestParam ShippingRecord.ShippingStatus status) {
        try {
            ShippingRecord shippingRecord = shippingService.updateShippingRecordStatus(id, status);
            return ResponseEntity.ok(shippingRecord);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("Error updating shipping record status", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal server error"));
        }
    }

    /**
     * Error response class
     */
    private static class ErrorResponse {
        private final String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}