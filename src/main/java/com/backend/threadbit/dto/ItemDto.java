package com.backend.threadbit.dto;


import com.backend.threadbit.config.CustomZonedDateTimeDeserializer;
import com.backend.threadbit.model.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDto {


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Brand is required")
    private String brand;

    @NotNull(message = "Size is required")
    private List<Size> size;

    @NotNull(message = "Condition is required")
    private Condition condition;

    @NotBlank(message = "Color is required")
    private List<String> color;

    @NotNull(message = "Starting price is required")
    @Positive(message = "Starting price must be positive")
    private Double startingPrice;

    private Double currentPrice;
    private List<String> imageUrls;

    @NotBlank(message = "Seller ID is required")
    private String sellerId;

    private User seller;

    @NotBlank(message = "Category ID is required")
    private Integer categoryId;

    @NotNull(message = "End time is required")
    @JsonDeserialize(using = CustomZonedDateTimeDeserializer.class)
    private ZonedDateTime endTime;

    private LocalDateTime createdAt;
    private Status status;

    private Integer originalPrice ;
    private Integer buyNowPrice;

    @Builder.Default
    private ItemType itemType = ItemType.AUCTION;

    @Positive(message = "Stock quantity must be positive")
    private Integer stockQuantity = 1;

    public boolean isValid() {
        if (itemType == ItemType.AUCTION) {
            return endTime != null;
        } else if (itemType == ItemType.INSTANT_BUY) {
            return buyNowPrice != null && buyNowPrice > 0 && stockQuantity != null && stockQuantity > 0;
        }
        return true;
    }
}
