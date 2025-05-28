package com.backend.threadbit.service;

import com.backend.threadbit.dto.LocationDto;
import com.backend.threadbit.model.Location;
import com.backend.threadbit.model.User;
import com.backend.threadbit.repository.LocationRepository;
import com.backend.threadbit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    @Autowired
    private final LocationRepository locationRepository;

    @Autowired
    private final UserRepository userRepository;

    @Override
    public LocationDto addUserLocation(String userId, LocationDto locationDto) {
        // Verify user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        // Create a new location
        Location location = Location.builder()
                .userId(userId)
                .address(locationDto.getAddress())
                .city(locationDto.getCity())
                .state(locationDto.getState())
                .country(locationDto.getCountry())
                .postalCode(locationDto.getPostalCode())
                .latitude(locationDto.getLatitude())
                .longitude(locationDto.getLongitude())
                .isCurrentLocation(locationDto.getIsCurrentLocation())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // If this is set as current location, unset any other current locations
        if (Boolean.TRUE.equals(location.getIsCurrentLocation())) {
            unsetCurrentLocations(userId);
        }

        // Save the location
        Location savedLocation = locationRepository.save(location);

        // Convert and return as LocationDto
        return convertToDto(savedLocation);
    }

    @Override
    public LocationDto updateUserLocation(String locationId, LocationDto locationDto) {
        // Find the location
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new EntityNotFoundException("Location not found with id: " + locationId));

        // Update location fields
        location.setAddress(locationDto.getAddress());
        location.setCity(locationDto.getCity());
        location.setState(locationDto.getState());
        location.setCountry(locationDto.getCountry());
        location.setPostalCode(locationDto.getPostalCode());
        location.setLatitude(locationDto.getLatitude());
        location.setLongitude(locationDto.getLongitude());

        // If this is being set as current location, unset any other current locations
        if (Boolean.TRUE.equals(locationDto.getIsCurrentLocation()) && !Boolean.TRUE.equals(location.getIsCurrentLocation())) {
            unsetCurrentLocations(location.getUserId());
            location.setIsCurrentLocation(true);
        } else {
            location.setIsCurrentLocation(locationDto.getIsCurrentLocation());
        }

        location.setUpdatedAt(LocalDateTime.now());

        // Save the location
        Location updatedLocation = locationRepository.save(location);

        // Convert and return as LocationDto
        return convertToDto(updatedLocation);
    }

    @Override
    public List<LocationDto> getUserLocations(String userId) {
        // Verify user exists
        userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        // Get all user's locations
        List<Location> locations = locationRepository.findByUserId(userId);

        // Convert and return as list of LocationDto
        return locations.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public LocationDto getLocationById(String locationId) {
        // Find the location
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new EntityNotFoundException("Location not found with id: " + locationId));

        // Convert and return as LocationDto
        return convertToDto(location);
    }

    @Override
    public LocationDto getCurrentUserLocation(String userId) {
        // Verify user exists
        userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        // Get user's current location
        Location location = locationRepository.findByUserIdAndIsCurrentLocationTrue(userId)
                .orElse(null);

        // Return null if no current location found
        if (location == null) {
            return null;
        }

        // Convert and return as LocationDto
        return convertToDto(location);
    }

    @Override
    @Transactional
    public void deleteLocation(String locationId) {
        // Find the location
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new EntityNotFoundException("Location not found with id: " + locationId));

        // Delete the location
        locationRepository.delete(location);
    }

    @Override
    @Transactional
    public LocationDto setCurrentLocation(String locationId) {
        // Find the location
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new EntityNotFoundException("Location not found with id: " + locationId));

        // Unset any current locations for this user
        unsetCurrentLocations(location.getUserId());

        // Set this location as current
        location.setIsCurrentLocation(true);
        location.setUpdatedAt(LocalDateTime.now());

        // Save the location
        Location updatedLocation = locationRepository.save(location);

        // Convert and return as LocationDto
        return convertToDto(updatedLocation);
    }

    // For backward compatibility
    @Override
    public LocationDto saveUserLocation(String userId, LocationDto locationDto) {
        // Always create a new location instead of updating existing ones
        // This ensures that multiple locations can be saved for a user

        // If isCurrentLocation is true, we need to unset any other current locations
        if (Boolean.TRUE.equals(locationDto.getIsCurrentLocation())) {
            unsetCurrentLocations(userId);
        } else {
            // If not explicitly set as current, check if this is the first location
            List<Location> existingLocations = locationRepository.findByUserId(userId);
            if (existingLocations.isEmpty()) {
                // If this is the first location, set it as current
                locationDto.setIsCurrentLocation(true);
            }
        }

        // Create a new location
        return addUserLocation(userId, locationDto);
    }

    // For backward compatibility
    @Override
    public LocationDto getUserLocation(String userId) {
        return getCurrentUserLocation(userId);
    }

    private void unsetCurrentLocations(String userId) {
        // Get all user's locations
        List<Location> locations = locationRepository.findByUserId(userId);

        // Unset current location flag for all locations
        for (Location loc : locations) {
            if (Boolean.TRUE.equals(loc.getIsCurrentLocation())) {
                loc.setIsCurrentLocation(false);
                loc.setUpdatedAt(LocalDateTime.now());
                locationRepository.save(loc);
            }
        }
    }

    private LocationDto convertToDto(Location location) {
        return LocationDto.builder()
                .id(location.getId())
                .address(location.getAddress())
                .city(location.getCity())
                .state(location.getState())
                .country(location.getCountry())
                .postalCode(location.getPostalCode())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .isCurrentLocation(location.getIsCurrentLocation())
                .build();
    }
}
