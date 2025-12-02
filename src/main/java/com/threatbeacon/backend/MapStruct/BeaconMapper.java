package com.threatbeacon.backend.MapStruct;

import com.threatbeacon.backend.api.dto.MuteRequestDto;
import com.threatbeacon.backend.beacon.command.MuteCommand;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface BeaconMapper {
    MuteCommand toMuteCommand(MuteRequestDto dto);
}
