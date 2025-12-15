package com.threatbeacon.backend.incident;

import com.threatbeacon.backend.ai.IncidentInsightService;
import com.threatbeacon.backend.api.dto.IncidentInsightDto;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/incidents")
public class IncidentController {

    private final IncidentInsightService insightService;
    private final IncidentRepository incidentRepository;


    public IncidentController(IncidentInsightService insightService, IncidentRepository incidentRepository) {
        this.insightService = insightService;
        this.incidentRepository = incidentRepository;
    }

    @GetMapping
    public List<Incident> getAllIncidents() {
        return incidentRepository.findAll(Sort.by(Sort.Direction.DESC, "updatedAt"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getIncidentDetail(@PathVariable Long id) {
        return incidentRepository.findById(id)
                .map(incident -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("id", incident.getId());
                    response.put("type", incident.getType());
                    response.put("severity", incident.getSeverity());
                    response.put("status", incident.getStatus());
                    response.put("createdAt", incident.getCreatedAt());
                    response.put("updatedAt", incident.getUpdatedAt());
                    response.put("eventCount", incident.getEventCount());

                    response.put("mainIps", incident.getMainIps() != null && !incident.getMainIps().isEmpty()
                            ? Arrays.asList(incident.getMainIps().split(","))
                            : List.of());

                    response.put("countries", incident.getCountries() != null && !incident.getCountries().isEmpty()
                            ? Arrays.asList(incident.getCountries().split(","))
                            : List.of());

                    return ResponseEntity.ok((Object)response);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/insight")
    public IncidentInsightDto getIncidentInsight(@PathVariable Long id) {
        return insightService.generateInsight(id);
    }
}
