package com.threatbeacon.backend.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventDto {

    @NotBlank
    private String type;      // LOGIN_FAILED, HTTP_ERROR, etc.

    private String source;    // auth-service, firewall, etc.

    @NotBlank(message = "IP is required")
    private String ip;        // 192.168.1.50

    private String country;   // CO, US, RU

    @NotBlank(message = "Severity is required")
    private String severity;  // LOW, MEDIUM, HIGH

    private String metadata;  // "user=soc-demo"

}