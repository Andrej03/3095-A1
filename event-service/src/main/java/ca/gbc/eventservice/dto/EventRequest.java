package ca.gbc.eventservice.dto;

public record EventRequest(
        String eventName,
        String organizerId,  // Changed to String
        String eventType,
        Integer expectedAttendees
) { }
