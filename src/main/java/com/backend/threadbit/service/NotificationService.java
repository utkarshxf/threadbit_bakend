package com.backend.threadbit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

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

    private static final String SITE_NAME = "ThreadBit";
    private static final String SITE_LOGO_URL = "https://threadbit.com/logo.png"; // Replace with actual logo URL

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
    /**
     * Sends a beautifully designed HTML notification to a buyer about winning an auction
     * 
     * @param buyerEmail buyer's email address
     * @param buyerName buyer's name
     * @param itemTitle title of the item
     * @param itemImageUrls list of item image URLs
     * @param sellerName seller's name
     * @param sellerEmail seller's email
     * @param sellerPhone seller's phone number
     * @param sellerLocation seller's location
     */
    public void sendHtmlAuctionWinNotificationToBuyer(
            String buyerEmail, 
            String buyerName, 
            String itemTitle,
            List<String> itemImageUrls,
            String sellerName, 
            String sellerEmail, 
            String sellerPhone, 
            String sellerLocation) {

        String subject = "Congratulations! You won the auction for " + itemTitle;

        // Get the first image URL or use a placeholder
        String imageUrl = itemImageUrls != null && !itemImageUrls.isEmpty() 
                ? itemImageUrls.get(0) 
                : "https://via.placeholder.com/300x200?text=No+Image";

        String htmlBody = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "    <meta charset=\"UTF-8\">" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "    <title>" + subject + "</title>" +
                "    <style>" +
                "        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: 0 auto; padding: 20px; }" +
                "        .header { text-align: center; margin-bottom: 20px; }" +
                "        .logo { max-width: 150px; height: auto; }" +
                "        .product-image { width: 100%; max-width: 300px; height: auto; display: block; margin: 20px auto; border-radius: 8px; }" +
                "        h1 { color: #4a4a4a; font-size: 24px; margin-bottom: 20px; }" +
                "        .content { background-color: #f9f9f9; padding: 20px; border-radius: 8px; }" +
                "        .seller-info { background-color: #ffffff; padding: 15px; border-radius: 8px; margin-top: 20px; border: 1px solid #eee; }" +
                "        .seller-info h2 { font-size: 18px; margin-top: 0; color: #4a4a4a; }" +
                "        .footer { margin-top: 30px; text-align: center; font-size: 12px; color: #888; }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class=\"header\">" +
                "        <h1>" + SITE_NAME + "</h1>" +
                "    </div>" +
                "    <div class=\"content\">" +
                "        <h1>Congratulations, " + buyerName + "!</h1>" +
                "        <p>You are the highest bidder for the item:</p>" +
                "        <h2 style=\"text-align: center;\">" + itemTitle + "</h2>" +
                "        <img src=\"" + imageUrl + "\" alt=\"" + itemTitle + "\" class=\"product-image\">" +
                "        <div class=\"seller-info\">" +
                "            <h2>Seller Information</h2>" +
                "            <p><strong>Name:</strong> " + sellerName + "</p>" +
                "            <p><strong>Email:</strong> " + sellerEmail + "</p>" +
                "            <p><strong>Phone:</strong> " + sellerPhone + "</p>" +
                "        </div>" +
                "        <p style=\"margin-top: 20px;\">You will get an mail by the seller for delivery purposes with in few days.</p>" +
                "    </div>" +
                "    <div class=\"footer\">" +
                "        <p>Thank you for using our platform!</p>" +
                "        <p>Best regards,<br>The " + SITE_NAME + " Team</p>" +
                "    </div>" +
                "</body>" +
                "</html>";

        // Log the notification
        log.info("HTML NOTIFICATION TO BUYER: {}", buyerEmail);
        log.info("SUBJECT: {}", subject);

        // Send the HTML email
        emailService.sendHtmlEmail(buyerEmail, subject, htmlBody);
    }

    /**
     * Sends a beautifully designed HTML notification to a seller about their item being sold
     * 
     * @param sellerEmail seller's email address
     * @param sellerName seller's name
     * @param itemTitle title of the item
     * @param itemImageUrls list of item image URLs
     * @param buyerName buyer's name
     * @param buyerEmail buyer's email
     * @param buyerPhone buyer's phone number
     * @param buyerLocation buyer's location
     * @param finalPrice final selling price
     */
    public void sendHtmlAuctionWinNotificationToSeller(
            String sellerEmail, 
            String sellerName, 
            String itemTitle,
            List<String> itemImageUrls,
            String buyerName, 
            String buyerEmail, 
            String buyerPhone, 
            String buyerLocation,
            double finalPrice) {

        String subject = "Your item \"" + itemTitle + "\" has been sold!";

        // Get the first image URL or use a placeholder
        String imageUrl = itemImageUrls != null && !itemImageUrls.isEmpty() 
                ? itemImageUrls.get(0) 
                : "https://via.placeholder.com/300x200?text=No+Image";

        String htmlBody = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "    <meta charset=\"UTF-8\">" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "    <title>" + subject + "</title>" +
                "    <style>" +
                "        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: 0 auto; padding: 20px; }" +
                "        .header { text-align: center; margin-bottom: 20px; }" +
                "        .logo { max-width: 150px; height: auto; }" +
                "        .product-image { width: 100%; max-width: 300px; height: auto; display: block; margin: 20px auto; border-radius: 8px; }" +
                "        h1 { color: #4a4a4a; font-size: 24px; margin-bottom: 20px; }" +
                "        .content { background-color: #f9f9f9; padding: 20px; border-radius: 8px; }" +
                "        .price { font-size: 24px; font-weight: bold; color: #2e7d32; text-align: center; margin: 20px 0; }" +
                "        .buyer-info { background-color: #ffffff; padding: 15px; border-radius: 8px; margin-top: 20px; border: 1px solid #eee; }" +
                "        .buyer-info h2 { font-size: 18px; margin-top: 0; color: #4a4a4a; }" +
                "        .footer { margin-top: 30px; text-align: center; font-size: 12px; color: #888; }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class=\"header\">" +
                "        <h1>" + SITE_NAME + "</h1>" +
                "    </div>" +
                "    <div class=\"content\">" +
                "        <h1>Great news, " + sellerName + "!</h1>" +
                "        <p>Your item has been sold:</p>" +
                "        <h2 style=\"text-align: center;\">" + itemTitle + "</h2>" +
                "        <img src=\"" + imageUrl + "\" alt=\"" + itemTitle + "\" class=\"product-image\">" +
                "        <div class=\"price\">Sold for $" + finalPrice + "</div>" +
                "        <div class=\"buyer-info\">" +
                "            <h2>Buyer Information</h2>" +
                "            <p><strong>Name:</strong> " + buyerName + "</p>" +
                "            <p><strong>Email:</strong> " + buyerEmail + "</p>" +
                "            <p><strong>Phone:</strong> " + buyerPhone + "</p>" +
                "            <p><strong>Location:</strong> " + buyerLocation + "</p>" +
                "        </div>" +
                "        <p style=\"margin-top: 20px;\">Please add delivery details to this item or contact the buyer to arrange delivery.</p>" +
                "    </div>" +
                "    <div class=\"footer\">" +
                "        <p>Thank you for using our platform!</p>" +
                "        <p>Best regards,<br>The " + SITE_NAME + " Team</p>" +
                "    </div>" +
                "</body>" +
                "</html>";

        // Log the notification
        log.info("HTML NOTIFICATION TO SELLER: {}", sellerEmail);
        log.info("SUBJECT: {}", subject);

        // Send the HTML email
        emailService.sendHtmlEmail(sellerEmail, subject, htmlBody);
    }
    /**
     * Sends a notification to a buyer about a successful purchase
     * 
     * @param buyerEmail buyer's email address
     * @param buyerName buyer's name
     * @param itemTitle title of the item
     * @param quantity quantity purchased
     * @param pricePerUnit price per unit
     * @param totalPrice total price
     * @param sellerName seller's name
     * @param sellerEmail seller's email
     * @param sellerPhoneNumber seller's phone number
     */
    public void sendPurchaseNotificationToBuyer(
            String buyerEmail, 
            String buyerName, 
            String itemTitle,
            int quantity,
            double pricePerUnit,
            double totalPrice,
            String sellerName, 
            String sellerEmail, 
            String sellerPhoneNumber) {

        String subject = "Purchase Confirmation: " + itemTitle;

        String body = "Dear " + buyerName + ",\n\n" +
                "Thank you for your purchase of \"" + itemTitle + "\".\n\n" +
                "Purchase Details:\n" +
                "Quantity: " + quantity + "\n" +
                "Price Per Unit: $" + pricePerUnit + "\n" +
                "Total Price: $" + totalPrice + "\n\n" +
                "Seller Information:\n" +
                "Name: " + sellerName + "\n" +
                "Email: " + sellerEmail + "\n" +
                "Phone: " + sellerPhoneNumber + "\n\n" +
                "Please contact the seller to arrange delivery.\n\n" +
                "Thank you for using our platform!\n\n" +
                "Best regards,\n" +
                "The ThreadBit Team";

        // Log the notification
        log.info("PURCHASE NOTIFICATION TO BUYER: {}", buyerEmail);
        log.info("SUBJECT: {}", subject);
        log.info("BODY: {}", body);

        // Send the email
        emailService.sendSimpleEmail(buyerEmail, subject, body);
    }

    /**
     * Sends a notification to a seller about an item being purchased
     * 
     * @param sellerEmail seller's email address
     * @param sellerName seller's name
     * @param itemTitle title of the item
     * @param quantity quantity purchased
     * @param pricePerUnit price per unit
     * @param totalPrice total price
     * @param buyerName buyer's name
     * @param buyerEmail buyer's email
     * @param buyerPhoneNumber buyer's phone number
     */
    public void sendPurchaseNotificationToSeller(
            String sellerEmail, 
            String sellerName, 
            String itemTitle,
            int quantity,
            double pricePerUnit,
            double totalPrice,
            String buyerName, 
            String buyerEmail, 
            String buyerPhoneNumber) {

        String subject = "Your item \"" + itemTitle + "\" has been purchased!";

        String body = "Dear " + sellerName + ",\n\n" +
                "Great news! Your item \"" + itemTitle + "\" has been purchased.\n\n" +
                "Purchase Details:\n" +
                "Quantity: " + quantity + "\n" +
                "Price Per Unit: $" + pricePerUnit + "\n" +
                "Total Price: $" + totalPrice + "\n\n" +
                "Buyer Information:\n" +
                "Name: " + buyerName + "\n" +
                "Email: " + buyerEmail + "\n" +
                "Phone: " + buyerPhoneNumber + "\n\n" +
                "Please contact the buyer to arrange delivery.\n\n" +
                "Thank you for using our platform!\n\n" +
                "Best regards,\n" +
                "The ThreadBit Team";

        // Log the notification
        log.info("PURCHASE NOTIFICATION TO SELLER: {}", sellerEmail);
        log.info("SUBJECT: {}", subject);
        log.info("BODY: {}", body);

        // Send the email
        emailService.sendSimpleEmail(sellerEmail, subject, body);
    }
    /**
     * Sends a beautifully designed HTML notification to a buyer about a successful purchase
     * 
     * @param buyerEmail buyer's email address
     * @param buyerName buyer's name
     * @param itemTitle title of the item
     * @param itemImageUrls list of item image URLs
     * @param quantity quantity purchased
     * @param pricePerUnit price per unit
     * @param totalPrice total price
     * @param sellerName seller's name
     * @param sellerEmail seller's email
     * @param sellerPhoneNumber seller's phone number
     */
    public void sendHtmlPurchaseNotificationToBuyer(
            String buyerEmail, 
            String buyerName, 
            String itemTitle,
            List<String> itemImageUrls,
            int quantity,
            double pricePerUnit,
            double totalPrice,
            String sellerName, 
            String sellerEmail, 
            String sellerPhoneNumber) {

        String subject = "Purchase Confirmation: " + itemTitle;

        // Get the first image URL or use a placeholder
        String imageUrl = itemImageUrls != null && !itemImageUrls.isEmpty() 
                ? itemImageUrls.get(0) 
                : "https://via.placeholder.com/300x200?text=No+Image";

        String htmlBody = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "    <meta charset=\"UTF-8\">" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "    <title>" + subject + "</title>" +
                "    <style>" +
                "        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: 0 auto; padding: 20px; }" +
                "        .header { text-align: center; margin-bottom: 20px; }" +
                "        .logo { max-width: 150px; height: auto; }" +
                "        .product-image { width: 100%; max-width: 300px; height: auto; display: block; margin: 20px auto; border-radius: 8px; }" +
                "        h1 { color: #4a4a4a; font-size: 24px; margin-bottom: 20px; }" +
                "        .content { background-color: #f9f9f9; padding: 20px; border-radius: 8px; }" +
                "        .purchase-details { background-color: #ffffff; padding: 15px; border-radius: 8px; margin-top: 20px; border: 1px solid #eee; }" +
                "        .purchase-details h2 { font-size: 18px; margin-top: 0; color: #4a4a4a; }" +
                "        .price { font-size: 24px; font-weight: bold; color: #2e7d32; text-align: center; margin: 20px 0; }" +
                "        .seller-info { background-color: #ffffff; padding: 15px; border-radius: 8px; margin-top: 20px; border: 1px solid #eee; }" +
                "        .seller-info h2 { font-size: 18px; margin-top: 0; color: #4a4a4a; }" +
                "        .footer { margin-top: 30px; text-align: center; font-size: 12px; color: #888; }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class=\"header\">" +
                "        <h1>" + SITE_NAME + "</h1>" +
                "    </div>" +
                "    <div class=\"content\">" +
                "        <h1>Thank you for your purchase, " + buyerName + "!</h1>" +
                "        <h2 style=\"text-align: center;\">" + itemTitle + "</h2>" +
                "        <img src=\"" + imageUrl + "\" alt=\"" + itemTitle + "\" class=\"product-image\">" +
                "        <div class=\"purchase-details\">" +
                "            <h2>Purchase Details</h2>" +
                "            <p><strong>Quantity:</strong> " + quantity + "</p>" +
                "            <p><strong>Price Per Unit:</strong> $" + pricePerUnit + "</p>" +
                "            <p><strong>Total Price:</strong> $" + totalPrice + "</p>" +
                "        </div>" +
                "        <div class=\"seller-info\">" +
                "            <h2>Seller Information</h2>" +
                "            <p><strong>Name:</strong> " + sellerName + "</p>" +
                "            <p><strong>Email:</strong> " + sellerEmail + "</p>" +
                "            <p><strong>Phone:</strong> " + sellerPhoneNumber + "</p>" +
                "        </div>" +
                "        <p style=\"margin-top: 20px;\">You will get an mail by the seller for delivery purposes with in few days.</p>" +
                "    </div>" +
                "    <div class=\"footer\">" +
                "        <p>Thank you for using our platform!</p>" +
                "        <p>Best regards,<br>The " + SITE_NAME + " Team</p>" +
                "    </div>" +
                "</body>" +
                "</html>";

        // Log the notification
        log.info("HTML PURCHASE NOTIFICATION TO BUYER: {}", buyerEmail);
        log.info("SUBJECT: {}", subject);

        // Send the HTML email
        emailService.sendHtmlEmail(buyerEmail, subject, htmlBody);
    }

    /**
     * Sends a beautifully designed HTML notification to a seller about an item being purchased
     * 
     * @param sellerEmail seller's email address
     * @param sellerName seller's name
     * @param itemTitle title of the item
     * @param itemImageUrls list of item image URLs
     * @param quantity quantity purchased
     * @param pricePerUnit price per unit
     * @param totalPrice total price
     * @param buyerName buyer's name
     * @param buyerEmail buyer's email
     * @param buyerPhoneNumber buyer's phone number
     */
    public void sendHtmlPurchaseNotificationToSeller(
            String sellerEmail, 
            String sellerName, 
            String itemTitle,
            List<String> itemImageUrls,
            int quantity,
            double pricePerUnit,
            double totalPrice,
            String buyerName, 
            String buyerEmail, 
            String buyerPhoneNumber,
            String buyerLocation
    ) {

        String subject = "Your item \"" + itemTitle + "\" has been purchased!";

        // Get the first image URL or use a placeholder
        String imageUrl = itemImageUrls != null && !itemImageUrls.isEmpty() 
                ? itemImageUrls.get(0) 
                : "https://via.placeholder.com/300x200?text=No+Image";

        String htmlBody = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "    <meta charset=\"UTF-8\">" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "    <title>" + subject + "</title>" +
                "    <style>" +
                "        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: 0 auto; padding: 20px; }" +
                "        .header { text-align: center; margin-bottom: 20px; }" +
                "        .logo { max-width: 150px; height: auto; }" +
                "        .product-image { width: 100%; max-width: 300px; height: auto; display: block; margin: 20px auto; border-radius: 8px; }" +
                "        h1 { color: #4a4a4a; font-size: 24px; margin-bottom: 20px; }" +
                "        .content { background-color: #f9f9f9; padding: 20px; border-radius: 8px; }" +
                "        .purchase-details { background-color: #ffffff; padding: 15px; border-radius: 8px; margin-top: 20px; border: 1px solid #eee; }" +
                "        .purchase-details h2 { font-size: 18px; margin-top: 0; color: #4a4a4a; }" +
                "        .price { font-size: 24px; font-weight: bold; color: #2e7d32; text-align: center; margin: 20px 0; }" +
                "        .buyer-info { background-color: #ffffff; padding: 15px; border-radius: 8px; margin-top: 20px; border: 1px solid #eee; }" +
                "        .buyer-info h2 { font-size: 18px; margin-top: 0; color: #4a4a4a; }" +
                "        .footer { margin-top: 30px; text-align: center; font-size: 12px; color: #888; }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class=\"header\">" +
                "        <h1>" + SITE_NAME + "</h1>" +
                "    </div>" +
                "    <div class=\"content\">" +
                "        <h1>Great news, " + sellerName + "!</h1>" +
                "        <p>Your item has been purchased:</p>" +
                "        <h2 style=\"text-align: center;\">" + itemTitle + "</h2>" +
                "        <img src=\"" + imageUrl + "\" alt=\"" + itemTitle + "\" class=\"product-image\">" +
                "        <div class=\"purchase-details\">" +
                "            <h2>Purchase Details</h2>" +
                "            <p><strong>Quantity:</strong> " + quantity + "</p>" +
                "            <p><strong>Price Per Unit:</strong> $" + pricePerUnit + "</p>" +
                "            <p><strong>Total Price:</strong> $" + totalPrice + "</p>" +
                "        </div>" +
                "        <div class=\"buyer-info\">" +
                "            <h2>Buyer Information</h2>" +
                "            <p><strong>Name:</strong> " + buyerName + "</p>" +
                "            <p><strong>Email:</strong> " + buyerEmail + "</p>" +
                "            <p><strong>Phone:</strong> " + buyerPhoneNumber + "</p>" +
                "            <p><strong>Location:</strong> " + buyerLocation + "</p>" +
                "        </div>" +
                "        <p style=\"margin-top: 20px;\">Please add delivery details to this item or contact the buyer to arrange delivery.</p>" +
                "    </div>" +
                "    <div class=\"footer\">" +
                "        <p>Thank you for using our platform!</p>" +
                "        <p>Best regards,<br>The " + SITE_NAME + " Team</p>" +
                "    </div>" +
                "</body>" +
                "</html>";

        // Log the notification
        log.info("HTML PURCHASE NOTIFICATION TO SELLER: {}", sellerEmail);
        log.info("SUBJECT: {}", subject);

        // Send the HTML email
        emailService.sendHtmlEmail(sellerEmail, subject, htmlBody);
    }
}
