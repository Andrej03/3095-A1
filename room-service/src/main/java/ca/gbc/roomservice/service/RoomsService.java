package ca.gbc.roomservice.service;

import ca.gbc.roomservice.dto.RoomRequest;
import ca.gbc.roomservice.dto.RoomResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface RoomsService {
    RoomResponse createRoomStatus(RoomRequest roomRequest);
    List<RoomResponse> getAllRooms();
    Long updateRoomStatus(Long roomId, RoomRequest roomRequest);
    void deleteRoomStatus(Long roomId);
    boolean isRoomAvailable(Long roomId, LocalDateTime parse, LocalDateTime parse1);
}
