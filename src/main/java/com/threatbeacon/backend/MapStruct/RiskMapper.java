package com.threatbeacon.backend.MapStruct;

import com.threatbeacon.backend.api.dto.RiskStatusDto;
import com.threatbeacon.backend.risk.RiskLevel;
import com.threatbeacon.backend.risk.RiskStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.threatbeacon.backend.api.dto.RiskStatusDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RiskMapper {

    @Mapping(target = "level", source = "level")
    RiskStatusDto toDto(RiskStatus riskStatus);
}

