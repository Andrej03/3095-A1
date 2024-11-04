package ca.gbc.roomservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "rooms")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Rooms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String roomName;

    @Column(nullable = false)
    private int capacity;

    @Column
    private String features;

    @Column(nullable = false)
    private boolean available;
}
