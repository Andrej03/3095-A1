package ca.gbc.roomservice.dto;

public record RoomRequest(
        int id,
        String roomName,
        int capacity,
        String features,
        boolean available
) { }
