package com.backend.threadbit.controller;

import com.backend.threadbit.dto.BidItemDto;
import com.backend.threadbit.dto.BidStatistics;
import com.backend.threadbit.dto.UserBidsResponse;
import com.backend.threadbit.service.BidService;
import com.backend.threadbit.service.UserBidService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-bids")
@RequiredArgsConstructor
@CrossOrigin(
        origins = { "https://threadbitwebsite-fqj4l.ondigitalocean.app" , "http://192.168.32.1:5173", "https://secondhand-threads.vercel.app","http://10.244.72.46:8080"},
        allowCredentials = "true",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
        allowedHeaders = {"Content-Type", "Authorization"}
)
public class UserBidsController {

    @Autowired
    private final UserBidService userBidService;



    // Get all user bids categorized by status
    @GetMapping("/{userId}")
    public ResponseEntity<UserBidsResponse> getUserBids(@PathVariable String userId) {
        try {
            UserBidsResponse response = userBidService.getUserBids(userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get only active bids
    @GetMapping("/{userId}/active")
    public ResponseEntity<List<BidItemDto>> getActiveBids(@PathVariable String userId) {
        try {
            List<BidItemDto> activeBids = userBidService.getActiveBids(userId);
            return ResponseEntity.ok(activeBids);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get only won auctions
    @GetMapping("/{userId}/won")
    public ResponseEntity<List<BidItemDto>> getWonAuctions(@PathVariable String userId) {
        try {
            List<BidItemDto> wonAuctions = userBidService.getWonAuctions(userId);
            return ResponseEntity.ok(wonAuctions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get only lost auctions
    @GetMapping("/{userId}/lost")
    public ResponseEntity<List<BidItemDto>> getLostAuctions(@PathVariable String userId) {
        try {
            List<BidItemDto> lostAuctions = userBidService.getLostAuctions(userId);
            return ResponseEntity.ok(lostAuctions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get bid statistics
    @GetMapping("/{userId}/statistics")
    public ResponseEntity<BidStatistics> getBidStatistics(@PathVariable String userId) {
        try {
            UserBidsResponse userBids = userBidService.getUserBids(userId);
            
            double totalAmountBid = userBids.getActiveBids().stream()
                .mapToDouble(bid -> bid.getUserBid().getAmount())
                .sum() +
                userBids.getWonAuctions().stream()
                .mapToDouble(bid -> bid.getUserBid().getAmount())
                .sum() +
                userBids.getLostAuctions().stream()
                .mapToDouble(bid -> bid.getUserBid().getAmount())
                .sum();

            double totalAmountWon = userBids.getWonAuctions().stream()
                .mapToDouble(bid -> bid.getItem().getCurrentPrice())
                .sum();

            int totalBids = userBids.getTotalActiveBids() + 
                           userBids.getTotalWonAuctions() + 
                           userBids.getTotalLostAuctions();

            double winRate = totalBids > 0 ? 
                (double) userBids.getTotalWonAuctions() / totalBids * 100 : 0.0;

            BidStatistics statistics = BidStatistics.builder()
                .totalActiveBids(userBids.getTotalActiveBids())
                .totalWonAuctions(userBids.getTotalWonAuctions())
                .totalLostAuctions(userBids.getTotalLostAuctions())
                .totalAmountBid(totalAmountBid)
                .totalAmountWon(totalAmountWon)
                .winRate(winRate)
                .build();

            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Alternative endpoint that works with query parameter (matches your current frontend)
    @GetMapping
    public ResponseEntity<UserBidsResponse> getUserBidsByParam(@RequestParam String userId) {
        return getUserBids(userId);
    }
}