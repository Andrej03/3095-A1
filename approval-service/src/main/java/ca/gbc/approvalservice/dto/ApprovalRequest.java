package ca.gbc.approvalservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApprovalRequest {
    private String eventId; // The ID of the event to be approved
    private String userId; // The ID of the user requesting the approval
    private boolean approved; // Approval status
    private String comments; // Comments related to the approval
}
