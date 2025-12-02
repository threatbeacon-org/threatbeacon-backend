package com.threatbeacon.backend.controllertes;


import com.threatbeacon.backend.MapStruct.BeaconMapper;
import com.threatbeacon.backend.api.dto.MuteRequestDto;
import com.threatbeacon.backend.beacon.BeaconController;
import com.threatbeacon.backend.beacon.BeaconStateService;
import com.threatbeacon.backend.beacon.command.MuteCommand;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BeaconController.class)
@Import(BeaconTestConfig.class)   // Import the test configuration class
class BeaconControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BeaconStateService beaconStateService;

    @Autowired
    private BeaconMapper beaconMapper;

    @Test
    void testMuteBeacon() throws Exception {

        MuteCommand command = new MuteCommand();
        command.setMuted(true);

        when(beaconMapper.toMuteCommand(any())).thenReturn(command);

        mockMvc.perform(
                post("/api/beacon/mute")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"muted\": true}")
        ).andExpect(status().isNoContent());
    }
}