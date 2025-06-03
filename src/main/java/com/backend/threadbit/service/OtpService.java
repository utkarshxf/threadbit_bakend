package com.backend.threadbit.service;

import com.backend.threadbit.dto.OtpRequest;
import com.backend.threadbit.dto.OtpResponse;
import com.backend.threadbit.dto.OtpVerificationRequest;

public interface OtpService {
    /**
     * Sends an OTP to the provided phone number
     * @param request containing the phone number
     * @return response with status and session details
     */
    OtpResponse sendOtp(OtpRequest request);
    
    /**
     * Verifies the OTP entered by the user
     * @param request containing the phone number and OTP
     * @return response with status and JWT token if verification is successful
     */
    OtpResponse verifyOtp(OtpVerificationRequest request);
}