package com.backend.threadbit.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "items")
public class Item {

    @Id
    private String id;
    private String title;
    private String description;
    private String brand;
    private List<Size> size;
    private Condition condition;
    private List<String> color;
    private double startingPrice;
    private double currentPrice;
    private List<String> imageUrls;

    private String sellerId;
    @DBRef(lazy = true)
    private User seller;

    private Integer categoryId;
    @DBRef(lazy = true)
    private Category category;

    @Field("endTime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private ZonedDateTime endTime;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    private Status status;

    // NEW FIELDS FOR INSTANT BUY
    @Builder.Default
    private ItemType itemType = ItemType.AUCTION;

    @Builder.Default
    private Integer stockQuantity = 1;

    @Builder.Default
    private Integer soldQuantity = 0;

    private Integer originalPrice ;
    private Integer buyNowPrice;
}
