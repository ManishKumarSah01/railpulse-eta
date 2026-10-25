package com.railpulse.eta.dto;

import java.time.LocalTime;

public record RouteStopDto(
        Integer stopSequence,
        String stationCode,
        String stationName,
        LocalTime scheduledArrival,
        LocalTime scheduledDeparture
) {
}
