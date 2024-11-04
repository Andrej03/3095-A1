package ca.gbc.bookingservice.repository;

import ca.gbc.bookingservice.model.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends MongoRepository<Booking, String> {
    List<Booking> findByRoomIdAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(String roomId, LocalDateTime startTime, LocalDateTime endTime);
}
