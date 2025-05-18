// src/main/java/com/backend/threadbit/controller/BidController.java
package com.backend.threadbit.controller;


import com.backend.threadbit.service.BidService;
import com.backend.threadbit.dto.BidDto;
import com.backend.threadbit.model.Bid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/bids")
@Slf4j
@CrossOrigin(
        origins = {"https://threadbitwebsite-fqj4l.ondigitalocean.app" , "http://192.168.32.1:5173", "https://secondhand-threads.vercel.app","http://10.244.72.46:8080"},
        allowCredentials = "true",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
        allowedHeaders = {"Content-Type", "Authorization"}
)
@RequiredArgsConstructor
public class BidController {

    @Autowired
    private final BidService bidService;

    @GetMapping
    public ResponseEntity<List<Bid>> getBids(
            @RequestParam(required = false) String itemId,
            @RequestParam(required = false) String userId) {
        try {
            List<Bid> bids;
            
            if (itemId != null) {
                bids = bidService.getBidsByItem(itemId);
            } else if (userId != null) {
                bids = bidService.getBidsByUser(userId);
            } else {
                bids = bidService.getAllBids();
            }
            
            return ResponseEntity.ok(bids);
        } catch (Exception e) {
            log.error("Error getting bids", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createBid(@Valid @RequestBody BidDto bidDto) {
        try {
            Bid createdBid = bidService.createBid(bidDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBid);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("Error creating bid", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal server error"));
        }
    }

    private static class ErrorResponse {
        private final String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}