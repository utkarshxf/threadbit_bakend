package com.backend.threadbit.service;

import com.backend.threadbit.dto.BidDto;
import com.backend.threadbit.dto.UserDto;
import com.backend.threadbit.model.Bid;
import com.backend.threadbit.model.Item;
import com.backend.threadbit.model.Status;
import com.backend.threadbit.model.User;
import com.backend.threadbit.repository.BidRepository;
import com.backend.threadbit.repository.ItemRepository;
import com.backend.threadbit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
@RequiredArgsConstructor
public class BidServiceImpl implements BidService {

    @Autowired
    private final BidRepository bidRepository;
    @Autowired
    private final ItemRepository itemRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final UserService userService;


    @Override
    public List<Bid> getAllBids() {
        return bidRepository.findAll();
    }

    @Override
    public List<Bid> getBidsByItem(String itemId) {
        return bidRepository.findByItemId(itemId);
    }

    @Override
    public List<Bid> getBidsByUser(String userId) {
        return bidRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public Bid createBid(BidDto bidDto) {
        // Check if item exists
        Item item = itemRepository.findById(bidDto.getItemId())
                .orElseThrow(() -> new NoSuchElementException("Item not found"));

        // Check if item is still active
        if (item.getStatus() != Status.ACTIVE) {
            throw new IllegalStateException("Auction is no longer active");
        }

        // Check if bid amount is higher than current price
        if (bidDto.getAmount() <= item.getCurrentPrice()) {
            throw new IllegalArgumentException(
                    "Bid amount must be higher than current price of $" + item.getCurrentPrice());
        }

        // Check if user exists and get user
        User user = userRepository.findById(bidDto.getUserId())
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        // Check if user has sufficient wallet balance
        double walletBalance;
        try {
            walletBalance = Double.parseDouble(user.getWalletBalance());
        } catch (NumberFormatException e) {
            walletBalance = 0.0;
        }

        if (walletBalance < bidDto.getAmount()) {
            throw new IllegalStateException("Insufficient wallet balance to place bid");
        }

        // Get seller
        User seller = userRepository.findById(item.getSellerId())
                .orElseThrow(() -> new NoSuchElementException("Seller not found"));

        // Check if there's a previous bid
        List<Bid> previousBids = bidRepository.findByItemId(bidDto.getItemId());
        Bid previousHighestBid = null;
        User previousBidder = null;

        if (!previousBids.isEmpty()) {
            // Find the highest previous bid
            previousHighestBid = previousBids.stream()
                    .max((b1, b2) -> Double.compare(b1.getAmount(), b2.getAmount()))
                    .orElse(null);

            if (previousHighestBid != null) {
                // Get the previous bidder
                previousBidder = userRepository.findById(previousHighestBid.getUserId())
                        .orElse(null);
            }
        }

        // Subtract bid amount from user's wallet
        double newUserBalance = walletBalance - bidDto.getAmount();
        UserDto userDto = UserDto.builder()
                .walletBalance(String.valueOf(newUserBalance))
                .build();
        userService.updateUser(user.getId(), userDto);

        // Handle previous bidder and seller
        if (previousBidder != null) {
            if (!previousBidder.getId().equals(user.getId())) {
                // Return the previous bid amount to the previous bidder (only if it's not the same user)
                double previousBidderBalance;
                try {
                    previousBidderBalance = Double.parseDouble(previousBidder.getWalletBalance());
                } catch (NumberFormatException e) {
                    previousBidderBalance = 0.0;
                }

                double newPreviousBidderBalance = previousBidderBalance + previousHighestBid.getAmount();
                UserDto previousBidderDto = UserDto.builder()
                        .walletBalance(String.valueOf(newPreviousBidderBalance))
                        .build();
                userService.updateUser(previousBidder.getId(), previousBidderDto);

                // Add the difference between current bid and previous bid to seller
                double sellerBalance;
                try {
                    sellerBalance = Double.parseDouble(seller.getWalletBalance());
                } catch (NumberFormatException e) {
                    sellerBalance = 0.0;
                }

                double newSellerBalance = sellerBalance + (bidDto.getAmount() - previousHighestBid.getAmount());
                UserDto sellerDto = UserDto.builder()
                        .walletBalance(String.valueOf(newSellerBalance))
                        .build();
                userService.updateUser(seller.getId(), sellerDto);
            } else {
                // Same user is bidding again, only add the difference to seller
                double sellerBalance;
                try {
                    sellerBalance = Double.parseDouble(seller.getWalletBalance());
                } catch (NumberFormatException e) {
                    sellerBalance = 0.0;
                }

                double newSellerBalance = sellerBalance + (bidDto.getAmount() - previousHighestBid.getAmount());
                UserDto sellerDto = UserDto.builder()
                        .walletBalance(String.valueOf(newSellerBalance))
                        .build();
                userService.updateUser(seller.getId(), sellerDto);
            }
        } else {
            // No previous bid, add all amount to seller
            double sellerBalance;
            try {
                sellerBalance = Double.parseDouble(seller.getWalletBalance());
            } catch (NumberFormatException e) {
                sellerBalance = 0.0;
            }

            double newSellerBalance = sellerBalance + bidDto.getAmount();
            UserDto sellerDto = UserDto.builder()
                    .walletBalance(String.valueOf(newSellerBalance))
                    .build();
            userService.updateUser(seller.getId(), sellerDto);
        }

        // Create and save the bid
        Bid bid = Bid.builder()
                .itemId(bidDto.getItemId())
                .userId(bidDto.getUserId())
                .amount(bidDto.getAmount())
                .build();

        // Update item's current price
        item.setCurrentPrice(bidDto.getAmount());
        itemRepository.save(item);

        return bidRepository.save(bid);
    }
}
