package com.railpulse.eta.dto;

import java.time.LocalDateTime;

public record EtaPredictionDto(
        Long id,
        String trainNumber,
        String trainName,
        String stationCode,
        String stationName,
        LocalDateTime scheduledArrival,
        LocalDateTime predictedArrival,
        Integer estimatedDelayMinutes,
        LocalDateTime lowerBound,
        LocalDateTime upperBound,
        LocalDateTime lastUpdated,
        String modelVersion,
        String dataSource,
        boolean simulated
) {
}
