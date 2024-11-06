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
    void setUp() {
        MockitoAnnotations.openMocks(this);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = now.plusDays(1).withSecond(0).withNano(0); // set to whole minute
        LocalDateTime endTime = startTime.plusHours(2);

        bookingRequest = new BookingRequest(1L, 1L, startTime, endTime, "Test Booking");
    }


    @Test
    void testCreateBookingWhenRoomIsAvailable() {
        // Arrange
        when(roomClient.isRoomAvailable(anyLong(), anyString(), anyString())).thenReturn(true);
        when(bookingRepository.save(any(Booking.class))).thenReturn(new Booking(1L, 1L, 1L, bookingRequest.startTime(), bookingRequest.endTime(), bookingRequest.purpose()));

        // Act
        BookingResponse bookingResponse = bookingService.createBooking(bookingRequest);

        // Assert
        assertNotNull(bookingResponse);
        assertEquals(1L, bookingResponse.id());
        verify(roomClient, times(1)).isRoomAvailable(anyLong(), anyString(), anyString());
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void testCreateBookingWhenRoomIsNotAvailable() {
        // Arrange
        when(roomClient.isRoomAvailable(anyLong(), anyString(), anyString())).thenReturn(false);

        // Act & Assert
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            bookingService.createBooking(bookingRequest);
        });
        assertEquals("Room is already booked for the requested time.", thrown.getMessage());
    }

    @Test
    void testGetAllBookings() {
        // Arrange
        Booking booking = new Booking(1L, 1L, 1L, bookingRequest.startTime(), bookingRequest.endTime(), bookingRequest.purpose());
        when(bookingRepository.findAll()).thenReturn(Collections.singletonList(booking));

        // Act
        List<BookingResponse> bookings = bookingService.getAllBookings();

        // Assert
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(booking.getId(), bookings.getFirst().id());
    }

    @Test
    void testCancelBooking() {
        // Arrange
        doNothing().when(bookingRepository).deleteById(anyString());

        // Act
        bookingService.cancelBooking("1");

        // Assert
        verify(bookingRepository, times(1)).deleteById("1");
    }
}
