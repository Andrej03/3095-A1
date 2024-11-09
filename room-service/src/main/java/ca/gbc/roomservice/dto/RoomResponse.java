package ca.gbc.roomservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public record RoomResponse(
        Long id,
        String roomName,
        Integer capacity,
        String features,
        boolean available
) { }
