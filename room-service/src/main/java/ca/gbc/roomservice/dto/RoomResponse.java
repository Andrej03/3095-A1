package ca.gbc.roomservice.dto;

public record RoomResponse(
        Long id,
        String roomName,
        Integer capacity,
        String features,
        boolean available
) { }
