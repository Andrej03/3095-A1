package ca.gbc.roomservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "t_rooms")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Rooms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomName;
    private int capacity;
    private String features;
    private boolean available;
}
