package ca.gbc.eventservice.dto;

public record EventResponse(
        Long id,
        String eventName,
        Long organizerId,
        String eventType,
        Integer expectedAttendees
) { }
