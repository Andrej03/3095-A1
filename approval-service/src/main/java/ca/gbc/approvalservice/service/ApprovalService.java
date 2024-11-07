package ca.gbc.approvalservice.service;

import ca.gbc.approvalservice.dto.ApprovalRequest;
import ca.gbc.approvalservice.dto.ApprovalResponse;

import java.util.List;

public interface ApprovalService {
    ApprovalResponse createApproval(ApprovalRequest approvalRequest);
    List<ApprovalResponse> getAllApprovals();
    ApprovalResponse getApprovalById(String approvalId);
    void deleteApproval(String approvalId);
}
