package com.threatbeacon.backend.risk;

// Importamos el objeto de DOMINIO, no el DTO
import java.time.ZonedDateTime;

import org.springframework.stereotype.Service;

@Service
public class RiskService {

    // Dependencias reales (Repositorios) se inyectarán aquí en el Sprint 2

    /**
     * Calcula el estado de riesgo global.
     * * CORRECCIÓN: Ahora devuelve 'RiskStatus' (Dominio) en lugar de 'RiskStatusDto'.
     * Esto permite que el Controller use el Mapper correctamente.
     */
    public RiskStatus calculateRiskStatus(boolean buzzerMuted) {

        // Lógica temporal para el Sprint 1 (Simulación)
        // Si está muteado -> NORMAL (Verde)
        // Si no está muteado -> SUSPICIOUS (Naranja) - Para probar que cambia
        RiskLevel level = buzzerMuted ? RiskLevel.NORMAL : RiskLevel.SUSPICIOUS;

        // Retornamos el objeto de dominio
        return new RiskStatus(
                level,
                buzzerMuted,
                ZonedDateTime.now()
        );
    }
}