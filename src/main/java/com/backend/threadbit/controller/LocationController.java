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

@RestController
@RequestMapping("/api/location")
@CrossOrigin(
        origins = { "https://threadbitwebsite-fqj4l.ondigitalocean.app" , "https://secondhand-threads.vercel.app" , "http://192.168.32.1:5173" ,"http://10.244.72.46:8080"},
        allowCredentials = "true",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
        allowedHeaders = {"Content-Type", "Authorization"}
)
@Slf4j
@RequiredArgsConstructor
public class LocationController {

    @Autowired
    private final LocationService locationService;

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

    @GetMapping("/{userId}")
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