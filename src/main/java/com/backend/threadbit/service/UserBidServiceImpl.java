package com.backend.threadbit.service;

import com.backend.threadbit.dto.BidItemDto;
import com.backend.threadbit.dto.UserBidsResponse;
import com.backend.threadbit.model.Bid;
import com.backend.threadbit.model.BidGroupResult;
import com.backend.threadbit.model.Item;
import com.backend.threadbit.repository.BidRepository;
import com.backend.threadbit.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserBidServiceImpl implements UserBidService {

    @Autowired
    private final BidRepository bidRepository;
    @Autowired
    private final ItemRepository itemRepository;

    @Override
    public UserBidsResponse getUserBids(String userId) {
        // Get user's highest bids per item
        List<Bid> userHighestBids = bidRepository.getUserHighestBidsPerItem(userId);
        
        if (userHighestBids.isEmpty()) {
            return UserBidsResponse.builder()
                .activeBids(Collections.emptyList())
                .wonAuctions(Collections.emptyList())
                .lostAuctions(Collections.emptyList())
                .build();
        }

        // Extract item IDs
        List<String> itemIds = userHighestBids.stream()
            .map(Bid::getItemId)
            .collect(Collectors.toList());

        // Get all items
        List<Item> items = itemRepository.findItemsByIds(itemIds);
        Map<String, Item> itemMap = items.stream()
            .collect(Collectors.toMap(Item::getId, item -> item));

        // Get bid groups to determine winners
        List<BidGroupResult> bidGroups = bidRepository.getBidsGroupedByItem(itemIds);
        Map<String, BidGroupResult> bidGroupMap = bidGroups.stream()
            .collect(Collectors.toMap(BidGroupResult::getId, group -> group));

        LocalDateTime now = LocalDateTime.now();
        
        List<BidItemDto> activeBids = new ArrayList<>();
        List<BidItemDto> wonAuctions = new ArrayList<>();
        List<BidItemDto> lostAuctions = new ArrayList<>();

        for (Bid userBid : userHighestBids) {
            Item item = itemMap.get(userBid.getItemId());
            if (item == null) continue;

            BidGroupResult bidGroup = bidGroupMap.get(userBid.getItemId());
            if (bidGroup == null) continue;

            boolean isAuctionEnded = now.isAfter(item.getEndTime());
            boolean isUserWinning = bidGroup.getHighestBid().getUserId().equals(userId);
            
            // Calculate user's rank
            int userRank = calculateUserRank(bidGroup.getAllBids(), userId);

            BidItemDto bidItemDto = BidItemDto.builder()
                .item(item)
                .userBid(userBid)
                .isWinning(isUserWinning)
                .userRank(userRank)
                .totalBids(bidGroup.getAllBids().size())
                .currentHighestBid(bidGroup.getHighestBid())
                .build();

            if (!isAuctionEnded) {
                activeBids.add(bidItemDto);
            } else if (isUserWinning) {
                wonAuctions.add(bidItemDto);
            } else {
                lostAuctions.add(bidItemDto);
            }
        }

        return UserBidsResponse.builder()
            .activeBids(activeBids)
            .wonAuctions(wonAuctions)
            .lostAuctions(lostAuctions)
            .build();
    }

    private int calculateUserRank(List<Bid> allBids, String userId) {
        // Sort bids by amount descending
        List<Bid> sortedBids = allBids.stream()
            .sorted((b1, b2) -> Double.compare(b2.getAmount(), b1.getAmount()))
            .collect(Collectors.toList());

        for (int i = 0; i < sortedBids.size(); i++) {
            if (sortedBids.get(i).getUserId().equals(userId)) {
                return i + 1; // Rank is 1-based
            }
        }
        return sortedBids.size(); // Fallback
    }
    @Override
    public List<BidItemDto> getActiveBids(String userId) {
        return getUserBids(userId).getActiveBids();
    }
    @Override
    public List<BidItemDto> getWonAuctions(String userId) {
        return getUserBids(userId).getWonAuctions();
    }
    @Override
    public List<BidItemDto> getLostAuctions(String userId) {
        return getUserBids(userId).getLostAuctions();
    }
}