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
public class UserResponseDto {
    private String id;
    private String username;
    private String email;
    private String avatarUrl;
    private LocalDateTime createdAt;
}