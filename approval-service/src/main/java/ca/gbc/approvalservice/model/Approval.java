package ca.gbc.approvalservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "approvals")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Approval {
    @Id
    private String id;
    private String eventId;
    private String userId;
    private boolean approved;
    private String comments; // Optional comments from staff
}
