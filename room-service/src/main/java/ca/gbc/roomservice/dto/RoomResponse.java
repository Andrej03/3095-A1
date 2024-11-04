package ca.gbc.roomservice.dto;

public record RoomResponse(
        int id,
        String roomName,
        int capacity,
        String features,
        boolean available
) { }
