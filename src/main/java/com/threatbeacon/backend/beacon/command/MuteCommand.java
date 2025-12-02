package com.threatbeacon.backend.beacon.command;

//
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MuteCommand {
    private boolean muted;

    public boolean isMuted() {
        return muted;
    }

}
