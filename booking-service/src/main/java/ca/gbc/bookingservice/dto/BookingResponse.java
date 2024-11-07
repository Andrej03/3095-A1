package ca.gbc.bookingservice.dto;

import java.time.LocalDateTime;

public record BookingResponse(
        String id,
        Long userId,
        String roomId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String purpose
) { }
