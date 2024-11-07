package ca.gbc.bookingservice.repository;

import ca.gbc.bookingservice.model.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends MongoRepository<Booking, String> {  // Changed ID type to String
    List<Booking> findByRoomIdAndStartTimeAndEndTime(String roomId, LocalDateTime endTime, LocalDateTime startTime);
}
