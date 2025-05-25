package com.backend.threadbit.service;

import com.backend.threadbit.dto.BidItemDto;
import com.backend.threadbit.dto.UserBidsResponse;
import com.backend.threadbit.model.Bid;

import java.util.List;

public interface UserBidService {
     UserBidsResponse getUserBids(String userId);
     List<BidItemDto> getActiveBids(String userId);
     List<BidItemDto> getWonAuctions(String userId);
     List<BidItemDto> getLostAuctions(String userId);

}
