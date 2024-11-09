package ca.gbc.roomservice.service;

import ca.gbc.roomservice.dto.RoomRequest;
import ca.gbc.roomservice.dto.RoomResponse;
import ca.gbc.roomservice.model.Rooms;
import ca.gbc.roomservice.repository.RoomsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@ResponseStatus
public class RoomsServiceImp implements RoomsService {

    private final RoomsRepository roomsRepository;

    @Autowired
    public RoomsServiceImp(RoomsRepository roomsRepository) {
        this.roomsRepository = roomsRepository;
    }

    @Override
    public RoomResponse createRoomStatus(RoomRequest roomRequest) {
        Rooms room = new Rooms();
        room.setRoomName(roomRequest.roomName());
        room.setCapacity(roomRequest.capacity());
        room.setFeatures(roomRequest.features());
        room.setAvailable(true);

        roomsRepository.save(room);

        return new RoomResponse(room.getId(), room.getRoomName(), room.getCapacity(), room.getFeatures(), room.isAvailable());
    }

    @Override
    public List<RoomResponse> getAllRooms() {
        List<Rooms> rooms = roomsRepository.findAll();
        return rooms.stream()
                .map(room -> new RoomResponse(room.getId(), room.getRoomName(), room.getCapacity(), room.getFeatures(), room.isAvailable()))
                .toList();
    }

    @Override
    public Long updateRoomStatus(Long roomId, RoomRequest roomRequest) {
        Optional<Rooms> roomOptional = roomsRepository.findById(roomId);
        if (roomOptional.isPresent()) {
            Rooms room = roomOptional.get();
            room.setRoomName(roomRequest.roomName());
            room.setCapacity(roomRequest.capacity());
            room.setFeatures(roomRequest.features());
            roomsRepository.save(room);
            return room.getId();
        } else {
            throw new IllegalArgumentException("Room with ID " + roomId + " not found");
        }
    }

    @Override
    public void deleteRoomStatus(Long roomId) {
        Optional<Rooms> roomOptional = roomsRepository.findById(roomId);
        if (roomOptional.isPresent()) {
            roomsRepository.deleteById(roomId);
        } else {
            throw new IllegalArgumentException("Room with ID " + roomId + " not found");
        }
    }

    @Override
    public boolean isRoomAvailable(Long roomId, LocalDateTime startTime, LocalDateTime endTime) {
        List<Rooms> conflictingBookings = roomsRepository.findConflictingBookings(roomId, startTime, endTime);
        return conflictingBookings.isEmpty();
    }
}
