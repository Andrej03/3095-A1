package ca.gbc.approvalservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApprovalRequest {
    private Long eventId;
    private Long userId;
    private boolean approved;
    private String approvalStatus;
}
