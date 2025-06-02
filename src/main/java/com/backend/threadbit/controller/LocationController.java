package com.backend.threadbit.controller;

import com.backend.threadbit.dto.LocationDto;
import com.backend.threadbit.service.LocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/location")
@CrossOrigin(
        origins = { "https://threadbitwebsite-fqj4l.ondigitalocean.app" ,"https://threadbit.in" , "threadbit.in","www.threadbit.in", "https://secondhand-threads.vercel.app" , "http://192.168.32.1:5173" ,"http://10.244.72.46:8080"},
        allowCredentials = "true",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
        allowedHeaders = {"Content-Type", "Authorization"}
)
@Slf4j
@RequiredArgsConstructor
public class LocationController {

    @Autowired
    private final LocationService locationService;

    // For backward compatibility
    @PostMapping("/{userId}")
    public ResponseEntity<?> saveUserLocation(
            @PathVariable String userId,
            @Valid @RequestBody LocationDto locationDto) {
        try {
            LocationDto savedLocation = locationService.saveUserLocation(userId, locationDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedLocation);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("Error saving user location", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal server error"));
        }
    }

    // For backward compatibility
    @GetMapping("/{userId}/single")
    public ResponseEntity<?> getUserLocation(@PathVariable String userId) {
        try {
            LocationDto location = locationService.getUserLocation(userId);
            if (location == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Location not found for user: " + userId));
            }
            return ResponseEntity.ok(location);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("Error getting user location", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal server error"));
        }
    }

    // New endpoints for multiple locations

    @PostMapping("/user/{userId}/add")
    public ResponseEntity<?> addUserLocation(
            @PathVariable String userId,
            @Valid @RequestBody LocationDto locationDto) {
        try {
            LocationDto savedLocation = locationService.addUserLocation(userId, locationDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedLocation);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("Error adding user location", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal server error"));
        }
    }

    @PutMapping("/{locationId}")
    public ResponseEntity<?> updateLocation(
            @PathVariable String locationId,
            @Valid @RequestBody LocationDto locationDto) {
        try {
            LocationDto updatedLocation = locationService.updateUserLocation(locationId, locationDto);
            return ResponseEntity.ok(updatedLocation);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("Error updating location", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal server error"));
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserLocations(@PathVariable String userId) {
        try {
            List<LocationDto> locations = locationService.getUserLocations(userId);
            return ResponseEntity.ok(locations);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("Error getting user locations", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal server error"));
        }
    }

    @GetMapping("/id/{locationId}")
    public ResponseEntity<?> getLocationById(@PathVariable String locationId) {
        try {
            LocationDto location = locationService.getLocationById(locationId);
            return ResponseEntity.ok(location);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("Error getting location by id", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal server error"));
        }
    }

    @GetMapping("/user/{userId}/current")
    public ResponseEntity<?> getCurrentUserLocation(@PathVariable String userId) {
        try {
            LocationDto location = locationService.getCurrentUserLocation(userId);
            if (location == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Current location not found for user: " + userId));
            }
            return ResponseEntity.ok(location);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("Error getting current user location", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal server error"));
        }
    }

    @DeleteMapping("/{locationId}")
    public ResponseEntity<?> deleteLocation(@PathVariable String locationId) {
        try {
            locationService.deleteLocation(locationId);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("Error deleting location", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal server error"));
        }
    }

    @PutMapping("/{locationId}/set-current")
    public ResponseEntity<?> setCurrentLocation(@PathVariable String locationId) {
        try {
            LocationDto location = locationService.setCurrentLocation(locationId);
            return ResponseEntity.ok(location);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("Error setting current location", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal server error"));
        }
    }

    private static class ErrorResponse {
        private final String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
