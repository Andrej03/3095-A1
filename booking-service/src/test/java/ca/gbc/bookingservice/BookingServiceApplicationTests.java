package ca.gbc.bookingservice;

import ca.gbc.bookingservice.client.RoomClient;
import ca.gbc.bookingservice.dto.BookingRequest;
import ca.gbc.bookingservice.dto.BookingResponse;
import ca.gbc.bookingservice.model.Booking;
import ca.gbc.bookingservice.repository.BookingRepository;
import ca.gbc.bookingservice.service.BookingServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class BookingServiceApplicationTests {

    @Mock
    private RoomClient roomClient;

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingServiceImp bookingService;

    private BookingRequest bookingRequest;

    @BeforeEach
    void setup() {
        // Mock the repository to return an empty list of bookings for the specific room.
        when(bookingRepository.findByRoomIdAndStartTimeAndEndTime(anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList()); // Room is available.
    }

    @Test
    void testCreateBookingWhenRoomIsAvailable() {
        // Arrange
        String expectedId = "generated-id-123"; // Mocked ID as a String (as MongoDB generates it)
        when(roomClient.isRoomAvailable(anyString(), anyString(), anyString())).thenReturn(true);

        // Mocking bookingRepository.save to return a booking with a generated String ID
        when(bookingRepository.save(any(Booking.class))).thenReturn(
                new Booking(expectedId, bookingRequest.userId(), bookingRequest.roomId(),
                        bookingRequest.startTime(), bookingRequest.endTime(), bookingRequest.purpose())
        );

        // Act
        BookingResponse bookingResponse = bookingService.createBooking(bookingRequest);

        // Assert
        assertNotNull(bookingResponse);
        assertEquals(expectedId, bookingResponse.id());
        verify(roomClient, times(1)).isRoomAvailable(anyString(), anyString(), anyString());
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void testCreateBookingWhenRoomIsNotAvailable() {
        // Arrange
        when(roomClient.isRoomAvailable(anyString(), anyString(), anyString())).thenReturn(false);

        // Act & Assert
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            bookingService.createBooking(bookingRequest);
        });
        assertEquals("Room is already booked for the requested time.", thrown.getMessage());
    }

    @Test
    void testGetAllBookings() {
        // Arrange
        String expectedId = "generated-id-123"; // Mocked ID as a String
        Booking booking = new Booking(expectedId, bookingRequest.userId(), bookingRequest.roomId(),
                bookingRequest.startTime(), bookingRequest.endTime(), bookingRequest.purpose());
        when(bookingRepository.findAll()).thenReturn(Collections.singletonList(booking));

        // Act
        List<BookingResponse> bookings = bookingService.getAllBookings();

        // Assert
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(expectedId, bookings.getFirst().id());  // Expect String ID
    }

    @Test
    void testCancelBooking() {
        // Act
        bookingService.cancelBooking("generated-id-123");  // Pass a String ID for MongoDB

        // Assert
        verify(bookingRepository, times(1)).deleteById("generated-id-123");  // Expect String ID
    }
}
