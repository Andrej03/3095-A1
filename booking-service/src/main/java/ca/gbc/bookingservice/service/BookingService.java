package ca.gbc.bookingservice.service;

import ca.gbc.bookingservice.dto.BookingRequest;
import ca.gbc.bookingservice.dto.BookingResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookingService {
    BookingResponse createBooking(BookingRequest bookingRequest);
    List<BookingResponse> getAllBookings();
    void cancelBooking(String bookingId);
}
