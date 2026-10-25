package com.railpulse.eta.dto;

public record DashboardSummaryDto(
        long totalTrains,
        long activeTrains,
        long openAlerts,
        String worstDelayTrain,
        String liveStatus,
        double averageDelayMinutes
) {
}
