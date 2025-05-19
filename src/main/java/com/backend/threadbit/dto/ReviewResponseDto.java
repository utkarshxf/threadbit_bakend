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
public class ReviewResponseDto {
    
    private String id;
    private String userId;      // The user being reviewed (seller)
    private String reviewerId;  // The user who wrote the review
    private String reviewerUsername; // Username of the reviewer
    
    private int rating;         // Rating from 1-5
    private String comment;     // Optional comment
    
    private LocalDateTime createdAt;
}