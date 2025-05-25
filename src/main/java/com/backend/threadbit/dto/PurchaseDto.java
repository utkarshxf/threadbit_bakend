package com.backend.threadbit.dto;

import com.backend.threadbit.model.Status;
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
public class PurchaseDto {
    
    private String id;
    
    @NotBlank(message = "Item ID is required")
    private String itemId;
    
    @NotBlank(message = "Buyer ID is required")
    private String buyerId;
    
    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    private Integer quantity;
    
    private Integer pricePerUnit;
    private Integer totalPrice;
    private LocalDateTime purchaseDate;
    private Status status;
}