package com.backend.threadbit.dto;


import com.backend.threadbit.model.Condition;
import com.backend.threadbit.model.Size;
import com.backend.threadbit.model.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDto {
    private String id;
    
    @NotBlank(message = "Title is required")
    private String title;
    
    @NotBlank(message = "Description is required")
    private String description;
    
    @NotBlank(message = "Brand is required")
    private String brand;
    
    @NotNull(message = "Size is required")
    private Size size;
    
    @NotNull(message = "Condition is required")
    private Condition condition;
    
    @NotBlank(message = "Color is required")
    private String color;
    
    @NotNull(message = "Starting price is required")
    @Positive(message = "Starting price must be positive")
    private Double startingPrice;
    
    private Double currentPrice;
    private List<String> imageUrls;
    
    @NotBlank(message = "Seller ID is required")
    private String sellerId;
    
    @NotBlank(message = "Category ID is required")
    private String categoryId;
    
    @NotNull(message = "End time is required")
    private LocalDateTime endTime;
    
    private LocalDateTime createdAt;
    private Status status;
}