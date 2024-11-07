package ca.gbc.bookingservice.service;

import ca.gbc.bookingservice.client.RoomClient;
import ca.gbc.bookingservice.dto.BookingRequest;
import ca.gbc.bookingservice.dto.BookingResponse;
import ca.gbc.bookingservice.model.Booking;
import ca.gbc.bookingservice.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImp implements BookingService {

    private static final Logger log = LoggerFactory.getLogger(BookingServiceImp.class);
    private final BookingRepository bookingRepository;
    private final RoomClient roomClient;

    @Override
    public BookingResponse createBooking(BookingRequest bookingRequest) {
        String startTimeStr = bookingRequest.startTime().toString();
        String endTimeStr = bookingRequest.endTime().toString();

        // Check if the room is available
        if (roomClient.isRoomAvailable(bookingRequest.roomId(), startTimeStr, endTimeStr)) {
            // Create the booking with the builder
            Booking booking = Booking.builder()
                    .userId(bookingRequest.userId())
                    .roomId(bookingRequest.roomId())
                    .startTime(bookingRequest.startTime())
                    .endTime(bookingRequest.endTime())
                    .purpose(bookingRequest.purpose())
                    .build();

            // Save the booking and allow MongoDB to generate the ID
            booking = bookingRepository.save(booking);

            log.info("New booking created: {}", booking);

            // Return the booking response with the ID from MongoDB
            return new BookingResponse(
                    booking.getId(),        // MongoDB-generated String ID
                    booking.getUserId(),
                    booking.getRoomId(),
                    booking.getStartTime(),
                    booking.getEndTime(),
                    booking.getPurpose()
            );
        } else {
            log.warn("Room is already booked for the requested time: {}", bookingRequest);
            throw new IllegalStateException("Room is already booked for the requested time.");
        }
    }

    @Override
    public List<BookingResponse> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        return bookings.stream()
                .map(booking -> new BookingResponse(booking.getId(), booking.getUserId(), booking.getRoomId(), booking.getStartTime(), booking.getEndTime(), booking.getPurpose()))
                .toList();
    }

    @Override
    public void cancelBooking(String bookingId) {
        log.debug("Cancelling booking with id {}", bookingId);
        bookingRepository.deleteById(bookingId);
    }
}
