package ca.gbc.eventservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventSet {

    private String eventName;
    private String eventType;
    private String eventDescription;
}
