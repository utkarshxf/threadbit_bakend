package com.backend.threadbit.service;

import com.backend.threadbit.dto.LocationDto;
import com.backend.threadbit.model.Location;

public interface LocationService {
    /**
     * Save or update a user's location
     * @param userId the ID of the user
     * @param locationDto the location data to save
     * @return the saved location
     */
    LocationDto saveUserLocation(String userId, LocationDto locationDto);
    
    /**
     * Get a user's location
     * @param userId the ID of the user
     * @return the user's location, or null if not found
     */
    LocationDto getUserLocation(String userId);
}