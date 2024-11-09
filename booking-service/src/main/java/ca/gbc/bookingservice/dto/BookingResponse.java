package ca.gbc.bookingservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public record BookingResponse(
        String id,
        Long userId,
        String roomId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String purpose
) { }
