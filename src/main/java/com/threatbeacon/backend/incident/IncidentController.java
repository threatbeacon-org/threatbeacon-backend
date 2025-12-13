package com.threatbeacon.backend.incident;


import com.threatbeacon.backend.ai.IncidentInsightService;
import com.threatbeacon.backend.api.dto.IncidentInsightDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/incidents")
public class IncidentController {

    private final IncidentInsightService insightService;

    public IncidentController(IncidentInsightService insightService) {
        this.insightService = insightService;
    }

    @GetMapping("/{id}/insights")
    public IncidentInsightDto getIncidentInsight(@PathVariable Long id) {
        return insightService.generateInsight(id);
    }
}

