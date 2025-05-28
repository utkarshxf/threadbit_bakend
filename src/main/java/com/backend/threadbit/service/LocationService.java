package com.backend.threadbit.service;

import com.backend.threadbit.dto.LocationDto;
import com.backend.threadbit.model.Location;

import java.util.List;

public interface LocationService {
    /**
     * Save a new location for a user
     * @param userId the ID of the user
     * @param locationDto the location data to save
     * @return the saved location
     */
    LocationDto addUserLocation(String userId, LocationDto locationDto);

    /**
     * Update an existing location
     * @param locationId the ID of the location to update
     * @param locationDto the updated location data
     * @return the updated location
     */
    LocationDto updateUserLocation(String locationId, LocationDto locationDto);

    /**
     * Get all locations for a user
     * @param userId the ID of the user
     * @return list of the user's locations
     */
    List<LocationDto> getUserLocations(String userId);

    /**
     * Get a specific location by ID
     * @param locationId the ID of the location
     * @return the location, or null if not found
     */
    LocationDto getLocationById(String locationId);

    /**
     * Get a user's current location
     * @param userId the ID of the user
     * @return the user's current location, or null if not found
     */
    LocationDto getCurrentUserLocation(String userId);

    /**
     * Delete a location
     * @param locationId the ID of the location to delete
     */
    void deleteLocation(String locationId);

    /**
     * Set a location as the current location
     * @param locationId the ID of the location to set as current
     * @return the updated location
     */
    LocationDto setCurrentLocation(String locationId);

    /**
     * For backward compatibility
     * @param userId the ID of the user
     * @param locationDto the location data to save
     * @return the saved location
     */
    LocationDto saveUserLocation(String userId, LocationDto locationDto);

    /**
     * For backward compatibility
     * @param userId the ID of the user
     * @return the user's current location, or null if not found
     */
    LocationDto getUserLocation(String userId);
}
