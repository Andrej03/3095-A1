package ca.gbc.eventservice.repository;

import ca.gbc.eventservice.model.Events;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventRepository extends MongoRepository<Events, Long> {
    // You can add custom query methods here if needed
}
