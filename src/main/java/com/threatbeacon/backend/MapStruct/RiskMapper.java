package com.threatbeacon.backend.MapStruct;

import com.threatbeacon.backend.api.dto.RiskStatusDto;
import com.threatbeacon.backend.risk.RiskStatus;
import org.springframework.stereotype.Component;


@Mapper(componentModel = "spring")
public interface RiskMapper {

  //mapping...
    @Mapping(target = "updatedAt", source = "timestamp")
    RiskStatusDto toDto(RiskStatus riskStatus);
}
