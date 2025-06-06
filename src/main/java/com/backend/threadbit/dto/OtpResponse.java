package com.backend.threadbit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtpResponse {
    private String status;
    private String details;
    private String otp; // Only present in some responses
    
    // For JWT token response after successful verification
    private String token;
}