package ca.gbc.eventservice.dto;

public record EventResponse(
        String id,
        String eventName,
        String organizerId,
        String eventType,
        int expectedAttendees
) { }
