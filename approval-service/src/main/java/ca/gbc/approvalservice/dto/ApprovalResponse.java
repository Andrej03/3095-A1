package ca.gbc.approvalservice.dto;

public record ApprovalResponse(
        String id,
        String eventId,
        String userId,
        boolean approved,
        String comments
) { }
