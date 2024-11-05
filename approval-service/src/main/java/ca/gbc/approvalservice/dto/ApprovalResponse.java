package ca.gbc.approvalservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApprovalResponse {
    private Long id; // The ID of the approval
    private String eventId; // The ID of the event being approved
    private String userId; // The ID of the user who requested the approval
    private boolean approved; // Approval status
    private String comments; // Comments related to the approval

    public ApprovalResponse(Long id, String eventId, String userId, boolean approved, String comments) {
        this.id = id;
        this.eventId = eventId;
        this.userId = userId;
        this.approved = approved;
        this.comments = comments;
    }
}
