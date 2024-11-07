package ca.gbc.roomservice.dto;

public record RoomRequest(
        String roomName,
        Integer capacity,
        String features,
        boolean available
) { }
