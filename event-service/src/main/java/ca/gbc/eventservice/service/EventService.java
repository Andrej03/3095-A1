package ca.gbc.eventservice.service;

import ca.gbc.eventservice.dto.EventRequest;
import ca.gbc.eventservice.dto.EventResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EventService {
    EventResponse createEvent(EventRequest eventRequest);
    List<EventResponse> getAllEvents();
    EventResponse getEventById(String eventId);
    void deleteEvent(String eventId);
}
