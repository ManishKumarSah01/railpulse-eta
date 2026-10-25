package com.railpulse.eta.dto;

import java.util.List;

public record TrainDetailDto(
        Long id,
        String trainNumber,
        String trainName,
        boolean active,
        List<RouteStopDto> route,
        String dataSource,
        boolean simulated
) {
}
