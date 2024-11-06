package ca.gbc.bookingservice.dto;

import java.time.LocalDateTime;

public record BookingResponse(
        Long id,
        Long userId,
        Long roomId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String purpose
) { }
