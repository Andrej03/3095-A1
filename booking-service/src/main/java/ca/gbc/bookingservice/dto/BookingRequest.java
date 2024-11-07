package ca.gbc.bookingservice.dto;

import java.time.LocalDateTime;

public record BookingRequest(
        Long userId,
        String roomId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String purpose
) { }
