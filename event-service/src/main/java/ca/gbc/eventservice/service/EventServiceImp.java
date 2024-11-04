package ca.gbc.eventservice.service;

import ca.gbc.eventservice.dto.EventRequest;
import ca.gbc.eventservice.dto.EventResponse;
import ca.gbc.eventservice.model.Events;
import ca.gbc.eventservice.repository.EventRepository;
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

    @Override
    public EventResponse createEvent(EventRequest eventRequest) {
        Events event = Events.builder()
                .eventName(eventRequest.eventName())
                .organizerId(eventRequest.organizerId())
                .eventType(eventRequest.eventType())
                .expectedAttendees(eventRequest.expectedAttendees())
                .build();

        eventRepository.save(event);
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
    public EventResponse getEventById(String eventId) {
        Events event = eventRepository.findById(eventId).orElse(null);
        if (event != null) {
            return new EventResponse(event.getId(), event.getEventName(), event.getOrganizerId(), event.getEventType(), event.getExpectedAttendees());
        }
        return null;
    }

    @Override
    public void deleteEvent(String eventId) {
        log.debug("Deleting event with id {}", eventId);
        eventRepository.deleteById(eventId);
    }
}
