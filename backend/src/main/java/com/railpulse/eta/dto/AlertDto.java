package com.railpulse.eta.dto;

import java.time.LocalDateTime;

public record AlertDto(
        Long id,
        String trainNumber,
        String stationCode,
        String alertType,
        String message,
        LocalDateTime createdAt,
        boolean acknowledged
) {
}
