package com.railpulse.eta.dto;

public record ModelMetricDto(
        String modelVersion,
        String predictor,
        boolean loaded,
        double maeMinutes,
        double rmseMinutes,
        int generatedPredictions,
        String status
) {
}
