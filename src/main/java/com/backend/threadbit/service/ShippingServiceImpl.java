package com.backend.threadbit.service;

import com.backend.threadbit.dto.ShippingDetailsDto;
import com.backend.threadbit.model.*;
import com.backend.threadbit.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

/**
 * Implementation of the ShippingService interface
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ShippingServiceImpl implements ShippingService {

    @Autowired
    private final ShippingRecordRepository shippingRecordRepository;

    @Autowired
    private final ItemRepository itemRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final PurchaseRepository purchaseRepository;

    @Autowired
    private final BidRepository bidRepository;

    @Autowired
    private final EmailService emailService;

    @Autowired
    private final FileStorageService fileStorageService;

    @Override
    @Transactional
    public ShippingRecord addShippingDetails(ShippingDetailsDto shippingDetailsDto) {
        // Validate that item exists
        Item item = itemRepository.findById(shippingDetailsDto.getItemId())
                .orElseThrow(() -> new NoSuchElementException("Item not found"));

        // Validate that seller exists
        User seller = userRepository.findById(shippingDetailsDto.getSellerId())
                .orElseThrow(() -> new NoSuchElementException("Seller not found"));

        // Validate that buyer exists
        User buyer = userRepository.findById(shippingDetailsDto.getBuyerId())
                .orElseThrow(() -> new NoSuchElementException("Buyer not found"));

        // Validate that seller is the actual seller of the item
        if (!item.getSellerId().equals(seller.getId())) {
            throw new IllegalArgumentException("Seller is not the owner of the item");
        }

        // Validate purchase or bid if provided
        Purchase purchase = null;
        Bid bid = null;

        if (shippingDetailsDto.getPurchaseId() != null && !shippingDetailsDto.getPurchaseId().isEmpty()) {
            purchase = purchaseRepository.findById(shippingDetailsDto.getPurchaseId())
                    .orElseThrow(() -> new NoSuchElementException("Purchase not found"));

            // Validate that purchase is for the specified item
            if (!purchase.getItemId().equals(item.getId())) {
                throw new IllegalArgumentException("Purchase is not for the specified item");
            }

            // Validate that buyer is the actual buyer of the purchase
            if (!purchase.getBuyerId().equals(buyer.getId())) {
                throw new IllegalArgumentException("Buyer is not the buyer of the purchase");
            }
        }

        if (shippingDetailsDto.getBidId() != null && !shippingDetailsDto.getBidId().isEmpty()) {
            bid = bidRepository.findById(shippingDetailsDto.getBidId())
                    .orElseThrow(() -> new NoSuchElementException("Bid not found"));

            // Validate that bid is for the specified item
            if (!bid.getItemId().equals(item.getId())) {
                throw new IllegalArgumentException("Bid is not for the specified item");
            }

            // Validate that buyer is the actual bidder
            if (!bid.getUserId().equals(buyer.getId())) {
                throw new IllegalArgumentException("Buyer is not the bidder of the bid");
            }
        }

        // Save receipt image
        String receiptImageUrl = saveReceiptImage(shippingDetailsDto.getReceiptImage());

        // Create shipping record
        ShippingRecord shippingRecord = ShippingRecord.builder()
                .itemId(item.getId())
                .purchaseId(purchase != null ? purchase.getId() : null)
                .bidId(bid != null ? bid.getId() : null)
                .sellerId(seller.getId())
                .buyerId(buyer.getId())
                .trackingNumber(shippingDetailsDto.getTrackingNumber())
                .carrier(shippingDetailsDto.getCarrier())
                .shippingMethod(shippingDetailsDto.getShippingMethod())
                .additionalNotes(shippingDetailsDto.getAdditionalNotes())
                .receiptImageUrl(receiptImageUrl)
                .status(shippingDetailsDto.getStatus())
                .build();

        // Save shipping record
        ShippingRecord savedRecord = shippingRecordRepository.save(shippingRecord);

        // Send notification to buyer
        try {
            sendShippingNotificationToBuyer(savedRecord, item, seller, buyer);
        } catch (Exception e) {
            log.error("Failed to send shipping notification: {}", e.getMessage(), e);
        }

        return savedRecord;
    }

    @Override
    public ShippingRecord getShippingRecordById(String id) {
        return shippingRecordRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Shipping record not found"));
    }

    @Override
    public List<ShippingRecord> getShippingRecordsByItemId(String itemId) {
        return shippingRecordRepository.findByItemId(itemId);
    }

    @Override
    public List<ShippingRecord> getShippingRecordsByPurchaseId(String purchaseId) {
        return shippingRecordRepository.findByPurchaseId(purchaseId);
    }

    @Override
    public List<ShippingRecord> getShippingRecordsByBidId(String bidId) {
        return shippingRecordRepository.findByBidId(bidId);
    }

    @Override
    public List<ShippingRecord> getShippingRecordsBySellerId(String sellerId) {
        return shippingRecordRepository.findBySellerId(sellerId);
    }

    @Override
    public List<ShippingRecord> getShippingRecordsByBuyerId(String buyerId) {
        return shippingRecordRepository.findByBuyerId(buyerId);
    }

    @Override
    @Transactional
    public ShippingRecord updateShippingRecordStatus(String id, ShippingRecord.ShippingStatus status) {
        ShippingRecord shippingRecord = shippingRecordRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Shipping record not found"));

        shippingRecord.setStatus(status);
        shippingRecord.setUpdatedAt(LocalDateTime.now());

        return shippingRecordRepository.save(shippingRecord);
    }

    /**
     * Save receipt image to storage
     * 
     * @param base64Image base64 encoded image
     * @return URL of the saved image
     */
    private String saveReceiptImage(String base64Image) {
        try {
            // Remove data:image/jpeg;base64, prefix if present
            String imageData = base64Image;
            if (base64Image.contains(",")) {
                imageData = base64Image.split(",")[1];
            }

            // Decode base64 image
            byte[] imageBytes = Base64.getDecoder().decode(imageData);

            // Generate unique filename
            String filename = "receipt_" + UUID.randomUUID().toString() + ".jpg";

            // Save image using FileStorageService
            return fileStorageService.saveImage(imageBytes, filename);
        } catch (Exception e) {
            log.error("Failed to save receipt image: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to save receipt image", e);
        }
    }

    /**
     * Send shipping notification to buyer
     * 
     * @param shippingRecord the shipping record
     * @param item the item
     * @param seller the seller
     * @param buyer the buyer
     */
    private void sendShippingNotificationToBuyer(ShippingRecord shippingRecord, Item item, User seller, User buyer) {
        String subject = "Shipping Confirmation for " + item.getTitle();

        String htmlBody = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "    <meta charset=\"UTF-8\">" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "    <title>" + subject + "</title>" +
                "    <style>" +
                "        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: 0 auto; padding: 20px; }" +
                "        .header { text-align: center; margin-bottom: 20px; }" +
                "        .product-image { width: 100%; max-width: 300px; height: auto; display: block; margin: 20px auto; border-radius: 8px; }" +
                "        h1 { color: #4a4a4a; font-size: 24px; margin-bottom: 20px; }" +
                "        .content { background-color: #f9f9f9; padding: 20px; border-radius: 8px; }" +
                "        .shipping-details { background-color: #ffffff; padding: 15px; border-radius: 8px; margin-top: 20px; border: 1px solid #eee; }" +
                "        .shipping-details h2 { font-size: 18px; margin-top: 0; color: #4a4a4a; }" +
                "        .receipt-image { width: 100%; max-width: 300px; height: auto; display: block; margin: 20px auto; border-radius: 8px; }" +
                "        .footer { margin-top: 30px; text-align: center; font-size: 12px; color: #888; }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class=\"header\">" +
                "        <h1>ThreadBit</h1>" +
                "    </div>" +
                "    <div class=\"content\">" +
                "        <h1>Your item has been shipped, " + buyer.getUsername() + "!</h1>" +
                "        <h2 style=\"text-align: center;\">" + item.getTitle() + "</h2>" +
                "        <img src=\"" + (item.getImageUrls() != null && !item.getImageUrls().isEmpty() ? item.getImageUrls().get(0) : "https://via.placeholder.com/300x200?text=No+Image") + "\" alt=\"" + item.getTitle() + "\" class=\"product-image\">" +
                "        <div class=\"shipping-details\">" +
                "            <h2>Shipping Details</h2>" +
                "            <p><strong>Tracking Number:</strong> " + shippingRecord.getTrackingNumber() + "</p>" +
                "            <p><strong>Carrier:</strong> " + shippingRecord.getCarrier() + "</p>" +
                "            <p><strong>Shipping Method:</strong> " + shippingRecord.getShippingMethod() + "</p>" +
                "            <p><strong>Additional Notes:</strong> " + (shippingRecord.getAdditionalNotes() != null ? shippingRecord.getAdditionalNotes() : "None") + "</p>" +
                "        </div>" +
                "        <h3 style=\"text-align: center; margin-top: 20px;\">Receipt Image</h3>" +
                "        <img src=\"" + shippingRecord.getReceiptImageUrl() + "\" alt=\"Receipt\" class=\"receipt-image\">" +
                "        <p style=\"margin-top: 20px;\">If you have any questions, please contact the seller at " + seller.getEmail() + ".</p>" +
                "    </div>" +
                "    <div class=\"footer\">" +
                "        <p>Thank you for using our platform!</p>" +
                "        <p>Best regards,<br>The ThreadBit Team</p>" +
                "    </div>" +
                "</body>" +
                "</html>";

        // Send the HTML email
        emailService.sendHtmlEmail(buyer.getEmail(), subject, htmlBody);
    }
}
