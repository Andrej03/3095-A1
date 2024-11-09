package ca.gbc.approvalservice.service;

import ca.gbc.approvalservice.dto.ApprovalRequest;
import ca.gbc.approvalservice.dto.ApprovalResponse;
import ca.gbc.approvalservice.model.Approval;
import ca.gbc.approvalservice.repository.ApprovalRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApprovalServiceImp implements ApprovalService {

    private final ApprovalRepository approvalRepository;

    public ApprovalServiceImp(ApprovalRepository approvalRepository) {
        this.approvalRepository = approvalRepository;
    }

    @Override
    public ApprovalResponse createApproval(ApprovalRequest approvalRequest) {
        Approval approval = new Approval();
        approval.setEventId(approvalRequest.getEventId());
        approval.setUserId(approvalRequest.getUserId());
        approval.setApproved(approvalRequest.isApproved());
        approval.setApprovalStatus(approvalRequest.getApprovalStatus());

        Approval savedApproval = approvalRepository.save(approval);

        return new ApprovalResponse(savedApproval.getId(), savedApproval.getEventId(),
                savedApproval.getUserId(), savedApproval.isApproved(), savedApproval.getApprovalStatus());
    }

    @Override
    public ApprovalResponse getApprovalById(String approvalId) {
        Approval approval = approvalRepository.findById(approvalId).orElse(null);
        if (approval == null) {
            return null;
        }
        return new ApprovalResponse(approval.getId(), approval.getEventId(), approval.getUserId(),
                approval.isApproved(), approval.getApprovalStatus());
    }

    @Override
    public List<ApprovalResponse> getAllApprovals() {
        return approvalRepository.findAll().stream()
                .map(approval -> new ApprovalResponse(approval.getId(), approval.getEventId(),
                        approval.getUserId(), approval.isApproved(), approval.getApprovalStatus()))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteApproval(String approvalId) {
        approvalRepository.deleteById(approvalId);
    }
}
