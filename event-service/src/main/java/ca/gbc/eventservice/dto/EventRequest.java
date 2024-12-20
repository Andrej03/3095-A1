package ca.gbc.eventservice.dto;

public record EventRequest(
        String eventName,
        String organizerId,
        String eventType,
        Integer expectedAttendees
) { }
