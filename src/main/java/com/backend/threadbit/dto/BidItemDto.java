package com.backend.threadbit.dto;

import com.backend.threadbit.model.Bid;
import com.backend.threadbit.model.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BidItemDto {
    private Item item;
    private Bid userBid;
    private boolean isWinning;
    private int userRank;
    private int totalBids;
    private Bid currentHighestBid;
}