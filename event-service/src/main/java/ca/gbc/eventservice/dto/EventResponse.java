package ca.gbc.eventservice.dto;

public record EventResponse(
        String id,  // Changed to String
        String eventName,
        String organizerId,  // Changed to String
        String eventType,
        Integer expectedAttendees
) { }
