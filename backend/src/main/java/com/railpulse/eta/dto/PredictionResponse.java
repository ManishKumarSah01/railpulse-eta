package com.railpulse.eta.dto;

import java.time.LocalDateTime;

public record PredictionResponse(
        String status,
        String modelVersion,
        String source,
        boolean simulated,
        String trainNumber,
        String stationCode,
        LocalDateTime predictedArrival,
        LocalDateTime lowerBound,
        LocalDateTime upperBound,
        Integer estimatedDelayMinutes,
        String message
) {
}
