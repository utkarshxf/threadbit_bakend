package com.backend.threadbit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtpRequest {
    
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+[0-9]{1,15}$", message = "Phone number must be in international format (e.g., +919999999999)")
    private String phoneNumber;
}