package ca.gbc.approvalservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Approval {

    @Id
    private String id;
    private Long eventId;
    private Long userId;
    private boolean approved;
    private String approvalStatus;
}
