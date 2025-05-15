package com.backend.threadbit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {
    private String id;
    private String username;
    private String name;
    private String walletBalance;
    private String phoneNumber;
    private List<Map<String , String>> socialMedia;
    private String email;
    private String password;
    private String avatarUrl;
}