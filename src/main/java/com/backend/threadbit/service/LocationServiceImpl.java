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

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    @Autowired
    private final LocationRepository locationRepository;

    @Autowired
    private final UserRepository userRepository;

    @Override
    public LocationDto saveUserLocation(String userId, LocationDto locationDto) {
        // Verify user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        // Check if user already has a location
        Location location = locationRepository.findByUserId(userId)
                .orElse(Location.builder()
                        .userId(userId)
                        .createdAt(LocalDateTime.now())
                        .build());

        // Update location fields
        location.setAddress(locationDto.getAddress());
        location.setCity(locationDto.getCity());
        location.setState(locationDto.getState());
        location.setCountry(locationDto.getCountry());
        location.setPostalCode(locationDto.getPostalCode());
        location.setLatitude(locationDto.getLatitude());
        location.setLongitude(locationDto.getLongitude());
        location.setIsCurrentLocation(locationDto.getIsCurrentLocation());
        location.setUpdatedAt(LocalDateTime.now());

        // Save the location
        Location savedLocation = locationRepository.save(location);

        // Convert and return as LocationDto
        return convertToDto(savedLocation);
    }

    @Override
    public LocationDto getUserLocation(String userId) {
        // Verify user exists
        userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        // Get user's location
        Location location = locationRepository.findByUserId(userId)
                .orElse(null);

        // Return null if no location found
        if (location == null) {
            return null;
        }

        // Convert and return as LocationDto
        return convertToDto(location);
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