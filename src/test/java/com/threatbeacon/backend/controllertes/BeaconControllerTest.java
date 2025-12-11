package com.threatbeacon.backend.controllertes;

import com.threatbeacon.backend.MapStruct.BeaconMapper;
import com.threatbeacon.backend.beacon.BeaconController;
import com.threatbeacon.backend.beacon.BeaconStateService;
import com.threatbeacon.backend.beacon.command.MuteCommand;
import com.threatbeacon.backend.config.SecurityConfig; // Import main security config
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BeaconController.class)
@Import({BeaconTestConfig.class, SecurityConfig.class}) // Explicitly import SecurityConfig
class BeaconControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BeaconStateService beaconStateService;

    @Autowired
    private BeaconMapper beaconMapper;

    @Test
    void testMuteBeacon_withCorrectCredentials_shouldSucceed() throws Exception {
        // Arrange
        MuteCommand command = new MuteCommand();
        command.setMuted(true);
        when(beaconMapper.toMuteCommand(any())).thenReturn(command);

        // Act & Assert
        mockMvc.perform(
                post("/api/beacon/mute")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"muted\": true}")
                        .with(httpBasic("soc-demo", "demo123!"))
        ).andExpect(status().isNoContent());
    }

    @Test
    void testMuteBeacon_withWrongCredentials_shouldReturnUnauthorized() throws Exception {
        // Act & Assert
        mockMvc.perform(
                post("/api/beacon/mute")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"muted\": true}")
                        .with(httpBasic("soc-demo", "wrong-password"))
        ).andExpect(status().isUnauthorized());
    }

    @Test
    void testMuteBeacon_withoutCredentials_shouldReturnUnauthorized() throws Exception {
        // Act & Assert
        mockMvc.perform(
                post("/api/beacon/mute")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"muted\": true}")
        ).andExpect(status().isUnauthorized());
    }
}
