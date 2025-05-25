package com.backend.threadbit.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BidGroupResult {
    private String id; // itemId
    private Bid highestBid;
    private List<Bid> allBids;
}