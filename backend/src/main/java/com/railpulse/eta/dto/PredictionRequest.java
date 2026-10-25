package com.railpulse.eta.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PredictionRequest(
        @NotBlank String trainNumber,
        @NotBlank String stationCode,
        String source,
        Boolean simulated
) {
}
