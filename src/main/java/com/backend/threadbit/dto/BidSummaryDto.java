package com.backend.threadbit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BidSummaryDto {
    private String itemId;
    private String itemTitle;
    private String itemImageUrl;
    private double userBidAmount;
    private double currentPrice;
    private boolean isWinning;
    private int userRank;
    private LocalDateTime endTime;
    private LocalDateTime bidCreatedAt;
    private String status; // "ACTIVE", "WON", "LOST"
}