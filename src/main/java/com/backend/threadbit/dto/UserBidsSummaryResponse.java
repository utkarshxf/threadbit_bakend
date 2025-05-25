package com.backend.threadbit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserBidsSummaryResponse {
    private List<BidSummaryDto> activeBids;
    private List<BidSummaryDto> wonAuctions;
    private List<BidSummaryDto> lostAuctions;
    private BidStatistics statistics;
}
