package com.backend.threadbit.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "users")
public class User {

    @Id
    private String id;

    @Indexed(unique = true)
    private String username;
    private String name;
    private String walletBalance;
    private String phoneNumber;
    private List<Map<String , String>> socialMedia;
    private String email;
    private boolean isVerified;
    private String description;
    private String avatarUrl;

    // Additional fields for artist data
    private String birthDate;
    private String deathDate;
    private String nationality;
    private String notableWorks;
    private String artMovement;
    private String education;
    private String awards;
    private String artistId; // Original Neo4j artist ID

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
