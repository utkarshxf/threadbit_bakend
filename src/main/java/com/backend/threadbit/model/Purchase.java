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
@Document(collection = "purchases")
public class Purchase {

    @Id
    private String id;
    
    private String itemId;
    @DBRef(lazy = true)
    private Item item;
    
    private String buyerId;
    @DBRef(lazy = true)
    private User buyer;
    
    private Integer quantity;
    private Integer pricePerUnit;
    private Integer totalPrice;
    
    @Builder.Default
    private LocalDateTime purchaseDate = LocalDateTime.now();
    
    @Builder.Default
    private Status status = Status.COMPLETED;
}