package com.backend.threadbit.service;

import com.backend.threadbit.config.JwtConfig;
import com.backend.threadbit.dto.OtpRequest;
import com.backend.threadbit.dto.OtpResponse;
import com.backend.threadbit.dto.OtpVerificationRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Slf4j
public class OtpServiceImpl implements OtpService {

    private RestTemplate restTemplate =  new RestTemplate();
    private final WebClient webClient;
    private final JwtConfig jwtConfig;

    @Value("${twofactor.api.key:XXXX-XXXX-XXXX-XXXX-XXXX}")
    private String apiKey;

    @Value("${twofactor.api.url:https://2factor.in/API/V1}")
    private String apiUrl;

    @Value("${twofactor.otp.template.name:OTP1}")
    private String otpTemplateName;



    @Override
    public OtpResponse sendOtp(OtpRequest request) {
        try {
            String phoneNumber = request.getPhoneNumber();
            if (!phoneNumber.startsWith("+")) {
                phoneNumber = "+91" + phoneNumber;
            }

            String apiUrl = "https://2factor.in/API/V1/35af6092-613b-11ef-8b60-0200cd936042/SMS/"
                    + phoneNumber + "/AUTOGEN2/OTP1";

            // Create headers
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.set("User-Agent", "Mozilla/5.0");

            HttpEntity<String> entity = new HttpEntity<>(headers);

            // Use exchange instead of getForEntity
            ResponseEntity<String> response = restTemplate.exchange(
                    apiUrl, HttpMethod.GET, entity, String.class);

            log.info("API Response: {}", response.getBody());

            // Parse and return response
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(response.getBody());

            String status = jsonNode.get("Status").asText();

            if ("Success".equals(status)) {
                return OtpResponse.builder()
                        .Status("Success")
                        .Details("OTP sent successfully")
                        .build();
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "API Error: " + jsonNode.get("Details").asText());
            }

        } catch (JsonProcessingException e) {
            log.error("Failed to parse API response", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Invalid API response format");
        } catch (Exception e) {
            log.error("Error sending OTP", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to send OTP: " + e.getMessage());
        }
    }

    @Override
    public OtpResponse verifyOtp(OtpVerificationRequest request) {
        try {
            String phoneNumber = request.getPhoneNumber();
            if (!phoneNumber.startsWith("+")) {
                phoneNumber = "+91" + phoneNumber;
            }

            String apiUrl = "https://2factor.in/API/V1/35af6092-613b-11ef-8b60-0200cd936042/SMS/VERIFY3/"
                    + phoneNumber + "/" + request.getOtp();

            // Create headers
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.set("User-Agent", "Mozilla/5.0");

            HttpEntity<String> entity = new HttpEntity<>(headers);

            // Use exchange instead of getForEntity
            ResponseEntity<String> response = restTemplate.exchange(
                    apiUrl, HttpMethod.GET, entity, String.class);

            log.info("OTP Verification API Response: {}", response.getBody());

            // Parse and return response
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(response.getBody());

            String status = jsonNode.get("Status").asText();

            if ("Success".equals(status)) {
                // Generate JWT token for successful verification
                String token = jwtConfig.generateToken(request.getPhoneNumber());

                return OtpResponse.builder()
                        .Status("Success")
                        .Details("OTP verified successfully")
                        .Token(token)
                        .build();
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "OTP verification failed: " + jsonNode.get("Details").asText());
            }

        } catch (JsonProcessingException e) {
            log.error("Failed to parse OTP verification API response", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Invalid API response format");
        } catch (Exception e) {
            log.error("Error verifying OTP", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to verify OTP: " + e.getMessage());
        }
    }
}