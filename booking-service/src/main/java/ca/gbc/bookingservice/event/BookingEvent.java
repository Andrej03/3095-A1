package ca.gbc.bookingservice.event;

import ca.gbc.bookingservice.model.Booking;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingEvent {

    private Long eventId;
    private Booking booking;
}
