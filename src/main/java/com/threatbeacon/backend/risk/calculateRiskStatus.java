package com.threatbeacon.backend.risk;

import java.time.ZonedDateTime;




/// Complement about controller.
public class calculateRiskStatus {
    public RiskStatus calculateRiskStatus(boolean buzzerMuted) {
        RiskLevel level = buzzerMuted ? RiskLevel.NORMAL : RiskLevel.SUSPICIOUS;

        return new RiskStatus(
                level,
                buzzerMuted,
                ZonedDateTime.now()
        );
    }

}
