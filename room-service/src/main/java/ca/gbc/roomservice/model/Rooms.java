package ca.gbc.roomservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "rooms")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Rooms {

    @Id
    private String id;
    private String roomName;
    private int capacity;
    private String features;
}