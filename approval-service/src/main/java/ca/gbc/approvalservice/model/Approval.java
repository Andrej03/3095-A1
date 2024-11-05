package ca.gbc.approvalservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Approval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String eventId; // ID of the event being approved
    private String userId; // ID of the user who requested approval
    private boolean approved; // Approval status
    private String comments; // Comments or notes related to the approval
}
