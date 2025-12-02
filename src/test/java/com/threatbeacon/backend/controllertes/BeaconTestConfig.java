package com.threatbeacon.backend.controllertes;

import com.threatbeacon.backend.MapStruct.BeaconMapper;
import com.threatbeacon.backend.beacon.BeaconStateService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class BeaconTestConfig {

    @Bean
    BeaconStateService beaconStateService() {
        return Mockito.mock(BeaconStateService.class);
    }

    @Bean
    BeaconMapper beaconMapper() {
        return Mockito.mock(BeaconMapper.class);
    }
}