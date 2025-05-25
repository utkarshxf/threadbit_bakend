package com.backend.threadbit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BidStatistics {
    private int totalActiveBids;
    private int totalWonAuctions;
    private int totalLostAuctions;
    private double totalAmountBid;
    private double totalAmountWon;
    private double winRate;
}