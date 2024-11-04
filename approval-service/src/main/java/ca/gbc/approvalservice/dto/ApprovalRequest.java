package ca.gbc.approvalservice.dto;

public record ApprovalRequest(
        String eventId,
        String userId,
        boolean approved,
        String comments
) { }
