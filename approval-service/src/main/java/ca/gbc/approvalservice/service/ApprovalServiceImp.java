package ca.gbc.approvalservice.service;

import ca.gbc.approvalservice.dto.ApprovalRequest;
import ca.gbc.approvalservice.dto.ApprovalResponse;
import ca.gbc.approvalservice.model.Approval;
import ca.gbc.approvalservice.repository.ApprovalRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApprovalServiceImp implements ApprovalService {

    private static final Logger log = LoggerFactory.getLogger(ApprovalServiceImp.class);
    private final ApprovalRepository approvalRepository;

    @Override
    public ApprovalResponse createApproval(ApprovalRequest approvalRequest) {
        Approval approval = Approval.builder()
                .eventId(approvalRequest.eventId())
                .userId(approvalRequest.userId())
                .approved(approvalRequest.approved())
                .comments(approvalRequest.comments())
                .build();

        approvalRepository.save(approval);
        log.info("New approval created: {}", approval);
        return new ApprovalResponse(approval.getId(), approval.getEventId(), approval.getUserId(), approval.isApproved(), approval.getComments());
    }

    @Override
    public List<ApprovalResponse> getAllApprovals() {
        List<Approval> approvals = approvalRepository.findAll();
        return approvals.stream()
                .map(approval -> new ApprovalResponse(approval.getId(), approval.getEventId(), approval.getUserId(), approval.isApproved(), approval.getComments()))
                .toList();
    }

    @Override
    public ApprovalResponse getApprovalById(String approvalId) {
        Approval approval = approvalRepository.findById(approvalId).orElse(null);
        if (approval != null) {
            return new ApprovalResponse(approval.getId(), approval.getEventId(), approval.getUserId(), approval.isApproved(), approval.getComments());
        }
        return null;
    }

    @Override
    public void deleteApproval(String approvalId) {
        log.debug("Deleting approval with id {}", approvalId);
        approvalRepository.deleteById(approvalId);
    }
}
