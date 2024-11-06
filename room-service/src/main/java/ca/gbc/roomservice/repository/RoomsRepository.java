package ca.gbc.roomservice.repository;

import ca.gbc.roomservice.model.Rooms;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomsRepository extends JpaRepository<Rooms, Long> {
    //List<Rooms> findByAvailable(boolean available);
}
