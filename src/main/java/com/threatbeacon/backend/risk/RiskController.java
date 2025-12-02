package com.threatbeacon.backend.risk;

import com.threatbeacon.backend.MapStruct.RiskMapper;
import com.threatbeacon.backend.api.dto.RiskStatusDto;
import com.threatbeacon.backend.beacon.BeaconStateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RequiredArgsConstructor
@RestController
@RequestMapping("/api/risk")
public class RiskController {

    private final RiskService riskService;
    private final BeaconStateService beaconStateService;
    private final RiskMapper riskMapper;

    @GetMapping
    public ResponseEntity<RiskStatusDto> getGlobalRiskStatus() {
        boolean isMuted = beaconStateService.getBeaconState().isBuzzerMuted();
        RiskStatusDto riskStatusDto = riskService.calculateRiskStatus(isMuted);
        return ResponseEntity.ok(riskStatusDto);
    }
}





