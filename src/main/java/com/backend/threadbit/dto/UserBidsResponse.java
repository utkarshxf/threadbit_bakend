package com.backend.threadbit.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// UserBidsResponse.java
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserBidsResponse {
    private List<BidItemDto> activeBids;
    private List<BidItemDto> wonAuctions;
    private List<BidItemDto> lostAuctions;

    @JsonIgnore
    public int getTotalActiveBids() {
        return activeBids != null ? activeBids.size() : 0;
    }

    @JsonIgnore
    public int getTotalWonAuctions() {
        return wonAuctions != null ? wonAuctions.size() : 0;
    }

    @JsonIgnore
    public int getTotalLostAuctions() {
        return lostAuctions != null ? lostAuctions.size() : 0;
    }
}