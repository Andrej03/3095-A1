package ca.gbc.eventservice.service;

import ca.gbc.eventservice.dto.EventRequest;
import ca.gbc.eventservice.dto.EventResponse;
import ca.gbc.eventservice.model.Events;
import ca.gbc.eventservice.repository.EventRepository;
import ca.gbc.userservice.service.UserService; // Adjust the import if needed
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImp implements EventService {

    private static final Logger log = LoggerFactory.getLogger(EventServiceImp.class);
    private final EventRepository eventRepository;
    private final UserService userService;

    @Override
    public EventResponse createEvent(EventRequest eventRequest) {
        // Validate the organizer's role
        String userRole = userService.getUserRole(eventRequest.organizerId());  // Directly use Long
        if (userRole == null) {
            throw new IllegalArgumentException("Invalid organizer ID");
        }

        // Role-based restrictions
        if ("STUDENT".equals(userRole) && eventRequest.expectedAttendees() > 10) {
            throw new IllegalArgumentException("Students cannot organize events with more than 10 attendees.");
        }

        // Create and save the event
        Events event = Events.builder()
                .eventName(eventRequest.eventName())
                .organizerId(eventRequest.organizerId())
                .eventType(eventRequest.eventType())
                .expectedAttendees(eventRequest.expectedAttendees())
                .build();

        event = eventRepository.save(event);  // Save event and get the ID

        log.info("New event created: {}", event);
        return new EventResponse(event.getId(), event.getEventName(), event.getOrganizerId(), event.getEventType(), event.getExpectedAttendees());
    }


    @Override
    public List<EventResponse> getAllEvents() {
        List<Events> events = eventRepository.findAll();
        return events.stream()
                .map(event -> new EventResponse(event.getId(), event.getEventName(), event.getOrganizerId(), event.getEventType(), event.getExpectedAttendees()))
                .toList();
    }

    @Override
    public EventResponse getEventById(Long eventId) {
        Events event = eventRepository.findById(eventId).orElse(null);
        return event != null ? new EventResponse(event.getId(), event.getEventName(), event.getOrganizerId(), event.getEventType(), event.getExpectedAttendees()) : null;
    }

    @Override
    public void deleteEvent(Long eventId) {
        log.debug("Deleting event with id {}", eventId);
        eventRepository.deleteById(eventId);
    }
}
