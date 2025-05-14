package com.backend.threadbit.service;


import com.backend.threadbit.dto.BidDto;
import com.backend.threadbit.model.Bid;

import java.util.List;

public interface BidService {
    List<Bid> getAllBids();
    List<Bid> getBidsByItem(String itemId);
    List<Bid> getBidsByUser(String userId);
    Bid createBid(BidDto bidDto);
}