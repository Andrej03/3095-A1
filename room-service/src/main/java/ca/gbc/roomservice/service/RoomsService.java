package ca.gbc.roomservice.service;

import ca.gbc.roomservice.dto.RoomRequest;
import ca.gbc.roomservice.dto.RoomResponse;

import java.util.List;

public interface RoomsService {
    RoomResponse createRoomStatus(RoomRequest roomRequest);
    List<RoomResponse> getAllRooms();
    int updateRoomStatus(int roomId, RoomRequest roomRequest);
    void deleteRoomStatus(int roomId);
}
