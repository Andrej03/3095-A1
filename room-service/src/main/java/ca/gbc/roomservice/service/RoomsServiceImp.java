package ca.gbc.roomservice.service;

import ca.gbc.roomservice.dto.RoomRequest;
import ca.gbc.roomservice.dto.RoomResponse;
import ca.gbc.roomservice.model.Rooms;
import ca.gbc.roomservice.repository.RoomsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoomsServiceImp implements RoomsService {

    private final RoomsRepository roomsRepository;

    @Override
    public RoomResponse createRoomStatus(RoomRequest roomRequest) {
        log.debug("Creating a new room {}", roomRequest.roomName());

        Rooms room = Rooms.builder()
                .roomName(roomRequest.roomName())
                .capacity(roomRequest.capacity())
                .features(roomRequest.features())
                .available(roomRequest.available())
                .build();

        roomsRepository.save(room);
        log.info("New room {} is saved", room.getId());

        return mapToRoomResponse(room);
    }

    @Override
    public List<RoomResponse> getAllRooms() {
        log.debug("Getting all rooms");
        List<Rooms> rooms = roomsRepository.findAll();
        return rooms.stream().map(this::mapToRoomResponse).toList();
    }

    @Override
    public int updateRoomStatus(int roomId, RoomRequest roomRequest) {
        log.debug("Updating room with id {}", roomId);

        Rooms room = roomsRepository.findById(roomId).orElseThrow(() -> {
            log.error("Room with id {} not found", roomId);
            return new RuntimeException("Room not found");
        });

        room.setRoomName(roomRequest.roomName());
        room.setCapacity(roomRequest.capacity());
        room.setFeatures(roomRequest.features());
        room.setAvailable(roomRequest.available());

        roomsRepository.save(room);
        log.info("Room {} updated successfully", room.getId());

        return room.getId();
    }

    @Override
    public void deleteRoomStatus(int roomId) {
        log.debug("Deleting room with id {}", roomId);
        roomsRepository.deleteById(roomId);
        log.info("Room with id {} deleted successfully", roomId);
    }

    private RoomResponse mapToRoomResponse(Rooms room) {
        return new RoomResponse(
                room.getId(),
                room.getRoomName(),
                room.getCapacity(),
                room.getFeatures(),
                room.isAvailable()
        );
    }
}
