package com.threatbeacon.backend.ai;

import com.threatbeacon.backend.api.dto.IncidentInsightDto;
import com.threatbeacon.backend.incident.Incident;
import com.threatbeacon.backend.incident.IncidentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatResponse; // Importante
import org.springframework.ai.chat.prompt.Prompt; // Importante
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions; // Importante para configurar opciones
import org.springframework.stereotype.Service;

import java.time.Duration;
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
        String promptText = buildPrompt(incident);

        try {
            log.info("Generating AI insight for incident ID: {}", incidentId);

            OpenAiChatOptions options = OpenAiChatOptions.builder()
                    .withTemperature(0.4f)
                    .withMaxTokens(600)
                    .build();

            Prompt prompt = new Prompt(promptText, options);

            ChatResponse response = chatModel.call(prompt);

            String insight = response.getResult().getOutput().getContent();

            log.info("Successfully generated AI insight for incident ID: {}", incidentId);
            return new IncidentInsightDto(incidentId, insight);

        } catch (Exception e) {
            log.error("Failed to generate AI insight for incident ID: {}. Error: {}", incidentId, e.getMessage());
            return new IncidentInsightDto(incidentId, FALLBACK_TEXT);
        }
    }

    private String buildPrompt(Incident incident) {
        long durationMinutes = 0;
        if (incident.getCreatedAt() != null && incident.getUpdatedAt() != null) {
            durationMinutes = Duration.between(incident.getCreatedAt(), incident.getUpdatedAt()).toMinutes();
        }

        // --- PROMPT "SENIOR SOC ANALYST" --
        return String.format(
                """
                Act as a Senior Tier 3 SOC Analyst. Analyze the following security incident telemetry and generate a high-priority incident response report.
                
                INCIDENT TELEMETRY:
                - Incident Type: %s
                - Severity: %s
                - Status: %s
                - Event Count: %d
                - Main Threat Actor IPs: %s
                - Affected Geolocation: %s
                - Duration: %d minutes
                
                RESPONSE REQUIREMENTS:
                Provide a tactical report in strict Markdown format with the following three sections. Keep it concise, technical, and actionable.
                
                1. TACTICAL ANALYSIS
                Identify the likely attack vector (e.g., Credential Stuffing, DDoS, SQLi) and the potential intent based on the severity and event count. Mention if this matches known patterns (e.g., MITRE ATT&CK).
                
                2. IMMEDIATE CONTAINMENT (The "Kill Switch")
                List 3 specific, imperative commands or actions to stop the attack NOW.
                (Example: "Block Subnet X on Firewall", "Revoke Session ID", "Isolate Host").
                
                3. REMEDIATION & HARDENING
                Provide 2 brief recommendations to prevent recurrence (e.g., WAF rules, Rate Limiting adjustments).
                
                TONE: Professional, urgent, and direct. No filler words.
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