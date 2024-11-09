package ca.gbc.roomservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public record RoomRequest(
        String roomName,
        Integer capacity,
        String features,
        boolean available
) { }
