package com.backend.threadbit.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "reviews")
public class Review {

    @Id
    private String id;
    
    private String userId;      // The user being reviewed (seller)
    private String reviewerId;  // The user who wrote the review
    
    private int rating;         // Rating from 1-5
    private String comment;     // Optional comment
    
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}