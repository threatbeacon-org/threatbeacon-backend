package com.threatbeacon.backend.risk;

import com.threatbeacon.backend.MapStruct.BeaconMapper;
import com.threatbeacon.backend.MapStruct.RiskMapper;
import com.threatbeacon.backend.api.dto.MuteRequestDto;
import com.threatbeacon.backend.api.dto.RiskStatusDto;
import com.threatbeacon.backend.beacon.BeaconStateService;
import com.threatbeacon.backend.beacon.command.MuteCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//All: update
//I needed Mapper, Status, calculateRisk, and DTO. I made minor adjustments to address the controller's requirements.
//Read carefully the function of each controller.
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/risk")
public class RiskController {

    private final RiskService riskService;
    private final BeaconStateService beaconStateService;
    private final RiskMapper riskMapper;
    private final BeaconMapper beaconMapper;


    /// GET
    //This endpoint retrieves the current global risk status of the system.
    //It works by first checking whether the beacon's buzzer is muted, using BeaconStateService.
    //That muted state is then passed to RiskService, which calculates the appropriate risk level based on the system’s rules.
    //The resulting RiskStatus domain object is finally converted into a RiskStatusDto through RiskMapper, and the controller returns it as the API response...

    @GetMapping
    public ResponseEntity<RiskStatusDto> getGlobalRiskStatus() {
        boolean isMuted = beaconStateService.getBeaconState().isBuzzerMuted();
        RiskStatusDto riskStatusDto = riskService.calculateRiskStatus(isMuted);
        return ResponseEntity.ok(riskStatusDto);
    }

    //POST
    //This endpoint updates the muted state of the physical beacon.
    //The controller receives a MuteRequestDto, which is transformed into a MuteCommand using BeaconMapper.
    //The command is used by BeaconStateService to persist the new buzzer state.
    //After updating the state, the controller recalculates the overall risk through RiskService using the new muted value.
    //Finally, RiskMapper converts the updated RiskStatus into a RiskStatusDto, which is returned to the client.

    @PostMapping("/mute")
    public ResponseEntity<RiskStatusDto> muteBeacon(@RequestBody MuteRequestDto requestDto) {
        MuteCommand command = beaconMapper.toMuteCommand(requestDto);
        beaconStateService.setBuzzerMuted(command.isMuted());
        RiskStatus updatedStatus = riskService.calculateRiskStatus(command.isMuted());
        RiskStatusDto dto = riskMapper.toDto(updatedStatus);
        return ResponseEntity.ok(dto);
    }
}





