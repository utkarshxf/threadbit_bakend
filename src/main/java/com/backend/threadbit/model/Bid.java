package com.backend.threadbit.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "bids")
public class Bid {

    @Id
    private String id;
    
    private String itemId;
    
    private String userId;
    @DBRef(lazy = true)
    private User user;
    
    private double amount;
    
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}