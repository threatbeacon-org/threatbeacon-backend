package com.threatbeacon.backend.risk;

import com.threatbeacon.backend.beacon.BeaconStateService;
import com.threatbeacon.backend.incident.Incident;
import com.threatbeacon.backend.incident.IncidentRepository;
import com.threatbeacon.backend.incident.IncidentSeverity;
import com.threatbeacon.backend.incident.IncidentStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RiskServiceTest {

    @Mock
    private IncidentRepository incidentRepository;

    @Mock
    private BeaconStateService beaconStateService;

    @InjectMocks
    private RiskService riskService;

    @Test
    void calculateRiskStatus_shouldReturnNormalAndResetMute_whenNoActiveIncidents() {
        // Arrange: No active incidents
        when(incidentRepository.findAllByStatus(IncidentStatus.OPEN)).thenReturn(Collections.emptyList());

        // Act: Calculate risk, starting with buzzer muted
        RiskStatus riskStatus = riskService.calculateRiskStatus(true);

        // Assert
        assertEquals(RiskLevel.NORMAL, riskStatus.getLevel());
        assertFalse(riskStatus.isBuzzerMuted());
        verify(beaconStateService).setBuzzerMuted(false);
    }

    @Test
    void calculateRiskStatus_shouldReturnSuspicious_andPreserveMuteState() {
        // Arrange: Active incidents with LOW and MEDIUM severity
        List<Incident> incidents = List.of(
                Incident.builder().severity(IncidentSeverity.LOW).build(),
                Incident.builder().severity(IncidentSeverity.MEDIUM).build()
        );
        when(incidentRepository.findAllByStatus(IncidentStatus.OPEN)).thenReturn(incidents);

        // Act: Calculate risk, starting with buzzer muted
        RiskStatus riskStatus = riskService.calculateRiskStatus(true);

        // Assert
        assertEquals(RiskLevel.SUSPICIOUS, riskStatus.getLevel());
        // Verify that the mute state is preserved (it should still be true)
        assertTrue(riskStatus.isBuzzerMuted());
        // Verify that the service was NOT called to change the mute state
        verify(beaconStateService, never()).setBuzzerMuted(anyBoolean());
    }

    @Test
    void calculateRiskStatus_shouldReturnCritical_whenHighIncidentExists() {
        // Arrange: Active incidents including one with HIGH severity
        List<Incident> incidents = List.of(
                Incident.builder().severity(IncidentSeverity.LOW).build(),
                Incident.builder().severity(IncidentSeverity.HIGH).build()
        );
        when(incidentRepository.findAllByStatus(IncidentStatus.OPEN)).thenReturn(incidents);

        // Act
        RiskStatus riskStatus = riskService.calculateRiskStatus(false);

        // Assert
        assertEquals(RiskLevel.CRITICAL, riskStatus.getLevel());
        assertFalse(riskStatus.isBuzzerMuted());
    }

    @Test
    void calculateRiskStatus_shouldReturnCritical_whenCriticalIncidentExists() {
        // Arrange: Active incidents including one with CRITICAL severity
        List<Incident> incidents = List.of(
                Incident.builder().severity(IncidentSeverity.MEDIUM).build(),
                Incident.builder().severity(IncidentSeverity.CRITICAL).build()
        );
        when(incidentRepository.findAllByStatus(IncidentStatus.OPEN)).thenReturn(incidents);

        // Act
        RiskStatus riskStatus = riskService.calculateRiskStatus(true);

        // Assert
        assertEquals(RiskLevel.CRITICAL, riskStatus.getLevel());
        assertTrue(riskStatus.isBuzzerMuted());
    }
}
