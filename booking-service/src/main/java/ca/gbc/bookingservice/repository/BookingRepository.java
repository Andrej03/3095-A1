package ca.gbc.bookingservice.repository;

import ca.gbc.bookingservice.model.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookingRepository extends MongoRepository<Booking, String> {  // Changed ID type to String
    //List<Booking> findByRoomIdAndStartTimeAndEndTime(String roomId, LocalDateTime endTime, LocalDateTime startTime);
}
