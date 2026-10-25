package com.railpulse.eta.prediction;

import java.time.LocalDateTime;

public record PredictionResult(
        String trainNumber,
        String stationCode,
        String modelVersion,
        String source,
        boolean simulated,
        LocalDateTime predictedArrival,
        LocalDateTime lowerBound,
        LocalDateTime upperBound,
        Integer estimatedDelayMinutes,
        String status
) {
}
