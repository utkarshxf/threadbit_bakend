package com.backend.threadbit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service for sending notifications to users
 * This implementation sends emails and logs notifications
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    @Autowired
    private final EmailService emailService;

    /**
     * Sends a notification to a buyer about winning an auction
     * 
     * @param buyerEmail buyer's email address
     * @param buyerName buyer's name
     * @param itemTitle title of the item
     * @param sellerName seller's name
     * @param sellerEmail seller's email
     * @param sellerPhone seller's phone number
     * @param sellerLocation seller's location
     */
    public void sendAuctionWinNotificationToBuyer(
            String buyerEmail, 
            String buyerName, 
            String itemTitle, 
            String sellerName, 
            String sellerEmail, 
            String sellerPhone, 
            String sellerLocation) {

        String subject = "Congratulations! You won the auction for " + itemTitle;

        String body = "Dear " + buyerName + ",\n\n" +
                "Congratulations! You are the highest bidder for the item \"" + itemTitle + "\".\n\n" +
                "Seller Information:\n" +
                "Name: " + sellerName + "\n" +
                "Email: " + sellerEmail + "\n" +
                "Phone: " + sellerPhone + "\n" +
                "Location: " + sellerLocation + "\n\n" +
                "Please contact the seller to arrange payment and delivery.\n\n" +
                "Thank you for using our platform!\n\n" +
                "Best regards,\n" +
                "The ThreadBit Team";

        // Log the notification
        log.info("NOTIFICATION TO BUYER: {}", buyerEmail);
        log.info("SUBJECT: {}", subject);
        log.info("BODY: {}", body);

        // Send the email
        emailService.sendSimpleEmail(buyerEmail, subject, body);
    }

    /**
     * Sends a notification to a seller about their item being sold
     * 
     * @param sellerEmail seller's email address
     * @param sellerName seller's name
     * @param itemTitle title of the item
     * @param buyerName buyer's name
     * @param buyerEmail buyer's email
     * @param buyerPhone buyer's phone number
     * @param buyerLocation buyer's location
     * @param finalPrice final selling price
     */
    public void sendAuctionWinNotificationToSeller(
            String sellerEmail, 
            String sellerName, 
            String itemTitle, 
            String buyerName, 
            String buyerEmail, 
            String buyerPhone, 
            String buyerLocation,
            double finalPrice) {

        String subject = "Your item \"" + itemTitle + "\" has been sold!";

        String body = "Dear " + sellerName + ",\n\n" +
                "Great news! Your item \"" + itemTitle + "\" has been sold for $" + finalPrice + ".\n\n" +
                "Buyer Information:\n" +
                "Name: " + buyerName + "\n" +
                "Email: " + buyerEmail + "\n" +
                "Phone: " + buyerPhone + "\n" +
                "Location: " + buyerLocation + "\n\n" +
                "Please contact the buyer to arrange delivery.\n\n" +
                "Thank you for using our platform!\n\n" +
                "Best regards,\n" +
                "The ThreadBit Team";

        // Log the notification
        log.info("NOTIFICATION TO SELLER: {}", sellerEmail);
        log.info("SUBJECT: {}", subject);
        log.info("BODY: {}", body);

        // Send the email
        emailService.sendSimpleEmail(sellerEmail, subject, body);
    }
}
