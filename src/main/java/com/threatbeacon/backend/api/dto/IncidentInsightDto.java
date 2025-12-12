package com.threatbeacon.backend.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncidentInsightDto {
    private Long incidentId;
    private String insightText;
}
