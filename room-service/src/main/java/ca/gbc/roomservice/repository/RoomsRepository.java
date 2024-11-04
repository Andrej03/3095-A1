package ca.gbc.roomservice.repository;

import ca.gbc.roomservice.model.Rooms;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomsRepository extends JpaRepository<Rooms, Integer> {
    List<Rooms> findByAvailable(boolean available);
}
