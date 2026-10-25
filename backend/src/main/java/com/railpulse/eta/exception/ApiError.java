package com.railpulse.eta.exception;

import java.time.LocalDateTime;

public record ApiError(String message, String path, LocalDateTime timestamp) {
}
