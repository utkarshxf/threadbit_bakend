package com.backend.threadbit.service;

import com.backend.threadbit.dto.BidDto;
import com.backend.threadbit.model.Bid;
import com.backend.threadbit.model.Item;
import com.backend.threadbit.model.Status;
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
        
        // Check if user exists
        userRepository.findById(bidDto.getUserId())
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        
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