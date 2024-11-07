package ca.gbc.eventservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "events")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Events {
    @Id
    private String id;  // Change from Long to String
    private String eventName;
    private String organizerId;  // Change from Long to String
    private String eventType;
    private Integer expectedAttendees;
}

