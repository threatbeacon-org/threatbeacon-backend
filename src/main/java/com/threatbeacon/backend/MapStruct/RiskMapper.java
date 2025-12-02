package com.threatbeacon.backend.MapStruct;

import com.threatbeacon.backend.api.dto.RiskStatusDto;
import com.threatbeacon.backend.risk.RiskStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;



@Mapper(componentModel = "spring")
public interface RiskMapper {

    /**
     * Mapear RiskStatus -> RiskStatusDto
     * Asignamos updatedAt = timestamp para que el DTO tenga ambos campos coherentes.
     */
    @Mapping(target = "updatedAt", source = "timestamp")
    RiskStatusDto toDto(RiskStatus riskStatus);
}
