package com.backend.threadbit.controller;

import com.backend.threadbit.dto.OtpRequest;
import com.backend.threadbit.dto.OtpResponse;
import com.backend.threadbit.dto.OtpVerificationRequest;
import com.backend.threadbit.service.OtpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(
        origins = { "https://threadbitwebsite-fqj4l.ondigitalocean.app" ,"https://threadbit.in" , "threadbit.in","www.threadbit.in", "https://threadbid.in" , "threadbid.in","www.threadbid.in", "https://secondhand-threads.vercel.app" , "http://192.168.32.1:5173" ,"http://10.244.72.46:8080"},
        allowCredentials = "true",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
        allowedHeaders = {"Content-Type", "Authorization"}
)
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    private final OtpService otpService;

    @PostMapping("/send-otp")
    public ResponseEntity<OtpResponse> sendOtp(@Valid @RequestBody OtpRequest request) {
        try {
            OtpResponse response = otpService.sendOtp(request);
            return ResponseEntity.ok(response);
        } catch (ResponseStatusException e) {
            log.error("Error sending OTP", e);
            return ResponseEntity.status(e.getStatusCode())
                    .body(OtpResponse.builder()
                            .status("Error")
                            .details(e.getReason())
                            .build());
        } catch (Exception e) {
            log.error("Unexpected error sending OTP", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(OtpResponse.builder()
                            .status("Error")
                            .details("An unexpected error occurred")
                            .build());
        }
    }

    @PostMapping("/verify-number")
    public ResponseEntity<OtpResponse> verifyNumber(@Valid @RequestBody OtpVerificationRequest request) {
        try {
            OtpResponse response = otpService.verifyOtp(request);
            return ResponseEntity.ok(response);
        } catch (ResponseStatusException e) {
            log.error("Error verifying OTP", e);
            return ResponseEntity.status(e.getStatusCode())
                    .body(OtpResponse.builder()
                            .status("Error")
                            .details(e.getReason())
                            .build());
        } catch (Exception e) {
            log.error("Unexpected error verifying OTP", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(OtpResponse.builder()
                            .status("Error")
                            .details("An unexpected error occurred")
                            .build());
        }
    }
}