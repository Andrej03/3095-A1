package ca.gbc.roomservice.repository;

import ca.gbc.roomservice.model.Rooms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

public interface RoomsRepository extends JpaRepository<Rooms, Long> {
    List<Rooms> findConflictingBookings(@RequestParam("roomId") Long roomId, @RequestParam("startTime") LocalDateTime startTime, @RequestParam("endTime") LocalDateTime endTime);
}
