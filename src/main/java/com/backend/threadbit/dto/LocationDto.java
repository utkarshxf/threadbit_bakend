package com.backend.threadbit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationDto {
    private String id;
    
    @NotBlank(message = "Address is required")
    @Size(min = 5, message = "Address must be at least 5 characters")
    private String address;
    
    @NotBlank(message = "City is required")
    @Size(min = 2, message = "City must be at least 2 characters")
    private String city;
    
    private String state;
    
    @NotBlank(message = "Country is required")
    @Size(min = 2, message = "Country must be at least 2 characters")
    private String country;
    
    private String postalCode;
    private Double latitude;
    private Double longitude;
    
    @Builder.Default
    private Boolean isCurrentLocation = false;
}