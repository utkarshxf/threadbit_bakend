package com.backend.threadbit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtpResponse {
    @JsonProperty("Status")
    private String Status;

    @JsonProperty("Details")
    private String Details;

    @JsonProperty("Message")
    private String Message;

    @JsonProperty("Token")
    private String Token;
}