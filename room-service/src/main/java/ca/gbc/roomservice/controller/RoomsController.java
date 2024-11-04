package ca.gbc.roomservice.controller;

import ca.gbc.roomservice.dto.RoomRequest;
import ca.gbc.roomservice.dto.RoomResponse;
import ca.gbc.roomservice.service.RoomsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomsController {

    private final RoomsService roomsService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<RoomResponse> createRoom(@RequestBody RoomRequest roomRequest) {
        RoomResponse createdRoom = roomsService.createRoomStatus(roomRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/rooms/" + createdRoom.id());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON)
                .body(createdRoom);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<RoomResponse> getAllRooms() {
        return roomsService.getAllRooms();
    }

    @PutMapping("/{roomId}")
    public ResponseEntity<?> updateRoom(@PathVariable("roomId") int roomId,
                                        @RequestBody RoomRequest roomRequest) {
        int updatedRoomId = roomsService.updateRoomStatus(roomId, roomRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/rooms/" + updatedRoomId);
        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<?> deleteRoom(@PathVariable("roomId") int roomId) {
        roomsService.deleteRoomStatus(roomId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
