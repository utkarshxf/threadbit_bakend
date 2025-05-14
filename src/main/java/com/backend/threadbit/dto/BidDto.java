// src/main/java/com/backend/threadbit/dto/BidDto.java
package com.backend.threadbit.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BidDto {
    private String id;
    
    @NotBlank(message = "Item ID is required")
    private String itemId;
    
    @NotBlank(message = "User ID is required")
    private String userId;
    
    @NotNull(message = "Bid amount is required")
    @Positive(message = "Bid amount must be positive")
    private Double amount;
    
    private LocalDateTime createdAt;
}

