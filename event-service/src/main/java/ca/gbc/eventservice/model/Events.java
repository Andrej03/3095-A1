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
    private Long id;
    private String eventName;
    private Long organizerId;
    private String eventType;
    private Integer expectedAttendees;
}
