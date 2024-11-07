package ca.gbc.approvalservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApprovalRequest {
    private Long eventId;
    private Long userId;
    private boolean approved;
    private String approvalStatus;
}
