package com.threatbeacon.backend.ai;

import com.threatbeacon.backend.api.dto.IncidentInsightDto; // <-- Updated import path
import com.threatbeacon.backend.incident.Incident;
import com.threatbeacon.backend.incident.IncidentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class IncidentInsightService {

    private static final String FALLBACK_TEXT = "AI insight temporarily unavailable. Please check OpenAI connectivity.";

    private final IncidentRepository incidentRepository;
    private final OpenAiChatModel chatModel;

    public IncidentInsightDto generateInsight(Long incidentId) {
        Optional<Incident> incidentOpt = incidentRepository.findById(incidentId);

        if (incidentOpt.isEmpty()) {
            return new IncidentInsightDto(incidentId, "Incident not found.");
        }

        Incident incident = incidentOpt.get();
        String prompt = buildPrompt(incident);

        try {
            log.info("Generating AI insight for incident ID: {}", incidentId);
            String insight = chatModel.call(prompt);
            log.info("Successfully generated AI insight for incident ID: {}", incidentId);
            return new IncidentInsightDto(incidentId, insight);
        } catch (Exception e) {
            // Log the error for debugging, without exposing secrets in the response
            log.error("Failed to generate AI insight for incident ID: {}. Error: {}", incidentId, e.getMessage());
            // Return the fallback DTO
            return new IncidentInsightDto(incidentId, FALLBACK_TEXT);
        }
    }

    private String buildPrompt(Incident incident) {
        long durationMinutes = 0;
        if (incident.getCreatedAt() != null && incident.getUpdatedAt() != null) {
            durationMinutes = Duration.between(incident.getCreatedAt(), incident.getUpdatedAt()).toMinutes();
        }

        return String.format(
                """
                Generate a concise security incident report based on the following data:
                - Incident Type: %s
                - Severity: %s
                - Status: %s
                - Event Count: %d
                - Main IPs Involved: %s
                - Affected Countries: %s
                - Incident Duration (minutes): %d

                The report must be in English and contain two sections:
                1.  **Summary:** A 2-3 line summary of the incident.
                2.  **Recommendations:** 2-3 short, actionable recommendations for a security analyst.

                Do not include any introductory or concluding phrases.
                """,
                incident.getType(),
                incident.getSeverity(),
                incident.getStatus(),
                incident.getEventCount(),
                incident.getMainIps(),
                incident.getCountries(),
                durationMinutes
        );
    }
}
