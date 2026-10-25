package com.railpulse.eta.dto;

import java.time.LocalDateTime;

public record ApiHealthDto(String status, String mode, LocalDateTime timestamp, String message) {
}
