package com.backend.threadbit.service;

import com.backend.threadbit.model.*;
import com.backend.threadbit.repository.BidRepository;
import com.backend.threadbit.repository.ItemRepository;
import com.backend.threadbit.repository.UserRepository;
import com.backend.threadbit.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service for handling auction completion tasks
 * Checks for ended auctions, determines winners, and sends notifications
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AuctionCompletionService {

    private final ItemRepository itemRepository;
    private final BidRepository bidRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final NotificationService notificationService;
     private final EmailService emailService;

    /**
     * Scheduled task that runs every hour to check for ended auctions
     * Identifies auctions that have ended but haven't been processed yet
     * Determines winners and sends notifications
     */
    @Scheduled(fixedRate = 3600000) // Run every hour (3600000 ms)
    public void checkEndedAuctions() {
        log.info("Checking for ended auctions...");

        // Find active auction items that have ended
        List<Item> endedAuctions = findEndedAuctions();

        if (endedAuctions.isEmpty()) {
            log.info("No ended auctions found.");
            emailService.sendSimpleEmail("utkarshxf@gmail.com", "No auctions have ended yet." , "");
            return;
        }

        log.info("Found {} ended auctions to process", endedAuctions.size());

        // Process each ended auction
        for (Item item : endedAuctions) {
            processEndedAuction(item);
        }
    }

    /**
     * Finds auctions that have ended but are still marked as active
     * @return List of ended auction items
     */
    private List<Item> findEndedAuctions() {
        LocalDateTime now = LocalDateTime.now();

        // Find items where:
        // 1. Item type is AUCTION
        // 2. Status is ACTIVE
        // 3. End time is in the past
        return itemRepository.findAll().stream()
                .filter(item -> ItemType.AUCTION.equals(item.getItemType()))
                .filter(item -> Status.ACTIVE.equals(item.getStatus()))
                .filter(item -> item.getEndTime() != null && item.getEndTime().isBefore(now))
                .collect(Collectors.toList());
    }

    /**
     * Processes a single ended auction
     * Determines the winner and sends notifications
     * @param item The ended auction item
     */
    private void processEndedAuction(Item item) {
        log.info("Processing ended auction for item: {} (ID: {})", item.getTitle(), item.getId());

        // Find the highest bid for this item
        Optional<Bid> highestBidOpt = findHighestBid(item.getId());

        if (highestBidOpt.isPresent()) {
            // There is a winner
            Bid winningBid = highestBidOpt.get();

            // Update item status to ENDED
            item.setStatus(Status.ENDED);
            itemRepository.save(item);

            // Send notifications
            sendAuctionCompletionNotifications(item, winningBid);

            log.info("Auction completed for item: {}. Winner: {}, Winning bid: ${}", 
                    item.getTitle(), winningBid.getUserId(), winningBid.getAmount());
        } else {
            // No bids were placed
            log.info("Auction ended with no bids for item: {}", item.getTitle());

            // Update item status to ENDED
            item.setStatus(Status.ENDED);
            itemRepository.save(item);
        }
    }

    /**
     * Finds the highest bid for an item
     * @param itemId The item ID
     * @return Optional containing the highest bid, or empty if no bids
     */
    private Optional<Bid> findHighestBid(String itemId) {
        List<Bid> bids = bidRepository.findByItemId(itemId);

        if (bids.isEmpty()) {
            return Optional.empty();
        }

        // Find the bid with the highest amount
        return bids.stream()
                .max((bid1, bid2) -> Double.compare(bid1.getAmount(), bid2.getAmount()));
    }

    /**
     * Sends notifications to the buyer (winner) and seller
     * @param item The auction item
     * @param winningBid The winning bid
     */
    private void sendAuctionCompletionNotifications(Item item, Bid winningBid) {
        try {
            // Get buyer information
            Optional<User> buyerOpt = userRepository.findById(winningBid.getUserId());
            if (buyerOpt.isEmpty()) {
                log.error("Buyer not found for bid ID: {}. Cannot send notifications.", winningBid.getId());
                return;
            }
            User buyer = buyerOpt.get();

            // Get seller information
            Optional<User> sellerOpt = userRepository.findById(item.getSellerId());
            if (sellerOpt.isEmpty()) {
                log.error("Seller not found for item ID: {}. Cannot send notifications.", item.getId());
                return;
            }
            User seller = sellerOpt.get();

            // Get location information
            String buyerLocation = getBuyerLocationString(buyer.getId());
            String sellerLocation = getSellerLocationString(seller.getId());

            // Send notification to buyer
            notificationService.sendAuctionWinNotificationToBuyer(
                    buyer.getEmail(),
                    buyer.getName(),
                    item.getTitle(),
                    seller.getName(),
                    seller.getEmail(),
                    seller.getPhoneNumber(),
                    sellerLocation
            );

            // Send notification to seller
            notificationService.sendAuctionWinNotificationToSeller(
                    seller.getEmail(),
                    seller.getName(),
                    item.getTitle(),
                    buyer.getName(),
                    buyer.getEmail(),
                    buyer.getPhoneNumber(),
                    buyerLocation,
                    winningBid.getAmount()
            );

            log.info("Successfully sent auction completion notifications for item: {}", item.getTitle());
        } catch (Exception e) {
            log.error("Error sending auction completion notifications for item: {}", item.getTitle(), e);
        }
    }

    /**
     * Gets a formatted location string for a buyer
     * @param userId The buyer's user ID
     * @return Formatted location string
     */
    private String getBuyerLocationString(String userId) {
        Optional<Location> locationOpt = locationRepository.findByUserIdAndIsCurrentLocationTrue(userId);

        if (locationOpt.isPresent()) {
            Location location = locationOpt.get();
            return formatLocation(location);
        }

        return "Location not provided";
    }

    /**
     * Gets a formatted location string for a seller
     * @param userId The seller's user ID
     * @return Formatted location string
     */
    private String getSellerLocationString(String userId) {
        Optional<Location> locationOpt = locationRepository.findByUserIdAndIsCurrentLocationTrue(userId);

        if (locationOpt.isPresent()) {
            Location location = locationOpt.get();
            return formatLocation(location);
        }

        return "Location not provided";
    }

    /**
     * Formats a location object into a readable string
     * @param location The location object
     * @return Formatted location string
     */
    private String formatLocation(Location location) {
        StringBuilder sb = new StringBuilder();

        if (location.getAddress() != null && !location.getAddress().isEmpty()) {
            sb.append(location.getAddress());
        }

        if (location.getCity() != null && !location.getCity().isEmpty()) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(location.getCity());
        }

        if (location.getState() != null && !location.getState().isEmpty()) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(location.getState());
        }

        if (location.getCountry() != null && !location.getCountry().isEmpty()) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(location.getCountry());
        }

        if (location.getPostalCode() != null && !location.getPostalCode().isEmpty()) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(location.getPostalCode());
        }

        return sb.length() > 0 ? sb.toString() : "Location not provided";
    }
}
