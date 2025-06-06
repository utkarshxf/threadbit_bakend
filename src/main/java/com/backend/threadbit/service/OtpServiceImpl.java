package com.backend.threadbit.service;

import com.backend.threadbit.config.JwtConfig;
import com.backend.threadbit.dto.OtpRequest;
import com.backend.threadbit.dto.OtpResponse;
import com.backend.threadbit.dto.OtpVerificationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class OtpServiceImpl implements OtpService {

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
//        String url = "https://2factor.in/API/V1/${apiKey}/SMS/${fullPhoneNumber}/AUTOGEN2/${otpTemplateName}";
        String url = apiUrl + "/" + apiKey + "/SMS/" + request.getPhoneNumber() + "/AUTOGEN2/" + otpTemplateName;


        try {
            OtpResponse response = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(OtpResponse.class)
                    .block();

            if (response != null && "Success".equals(response.getStatus())) {
                return response;
            } else {
                log.error("Failed to send OTP: {}", response);
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Failed to send OTP");
            }
        } catch (Exception e) {
            log.error("Error sending OTP", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                    "Error sending OTP: " + e.getMessage());
        }
    }

    @Override
    public OtpResponse verifyOtp(OtpVerificationRequest request) {
        String url = String.format("%s/%s/SMS/VERIFY3/%s/%s", 
                apiUrl, apiKey, request.getPhoneNumber(), request.getOtp());
        
        try {
            OtpResponse response = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(OtpResponse.class)
                    .block();
            
            if (response != null && "Success".equals(response.getStatus())) {
                // Generate JWT token
                String token = jwtConfig.generateToken(request.getPhoneNumber());
                response.setToken(token);
                return response;
            } else {
                log.error("OTP verification failed: {}", response);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                        "OTP verification failed");
            }
        } catch (Exception e) {
            log.error("Error verifying OTP", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                    "Error verifying OTP: " + e.getMessage());
        }
    }
}