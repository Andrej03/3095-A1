package ca.gbc.bookingservice.service;

import ca.gbc.bookingservice.dto.BookingRequest;
import ca.gbc.bookingservice.dto.BookingResponse;
import ca.gbc.bookingservice.model.Booking;
import ca.gbc.bookingservice.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImp implements BookingService {

    private static final Logger log = LoggerFactory.getLogger(BookingServiceImp.class);
    private final BookingRepository bookingRepository;

    @Override
    public BookingResponse createBooking(BookingRequest bookingRequest) {
        if (isRoomAvailable(bookingRequest.roomId(), bookingRequest.startTime(), bookingRequest.endTime())) {
            Booking booking = Booking.builder()
                    .userId(bookingRequest.userId())
                    .roomId(bookingRequest.roomId())
                    .startTime(bookingRequest.startTime())
                    .endTime(bookingRequest.endTime())
                    .purpose(bookingRequest.purpose())
                    .build();

            bookingRepository.save(booking);
            log.info("New booking created: {}", booking);
            return new BookingResponse(booking.getId(), booking.getUserId(), booking.getRoomId(), booking.getStartTime(), booking.getEndTime(), booking.getPurpose());
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

    private boolean isRoomAvailable(String roomId, LocalDateTime startTime, LocalDateTime endTime) {
        List<Booking> conflictingBookings = bookingRepository.findByRoomIdAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(roomId, startTime, endTime);
        return conflictingBookings.isEmpty();
    }
}
