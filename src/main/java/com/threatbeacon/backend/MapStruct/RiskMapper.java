package com.threatbeacon.backend.MapStruct;

import com.threatbeacon.backend.api.dto.RiskStatusDto;
import com.threatbeacon.backend.risk.RiskStatus;
import org.springframework.stereotype.Component;


@Mapper(componentModel = "spring")
public interface RiskMapper {

    public RiskStatusDto toDto(RiskStatus riskStatus) {
        if (riskStatus == null) {
            return null;
        }

        return RiskStatusDto.builder()
                .level(riskStatus.getLevel())
                .buzzerMuted(riskStatus.isBuzzerMuted())
                .updatedAt(riskStatus.getUpdatedAt())
                .build();
    }
}