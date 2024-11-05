package ca.gbc.approvalservice.service;

import ca.gbc.approvalservice.dto.ApprovalRequest; // Import ApprovalRequest
import ca.gbc.approvalservice.dto.ApprovalResponse;
import ca.gbc.approvalservice.model.Approval;
import ca.gbc.approvalservice.repository.ApprovalRepository;
import ca.gbc.eventservice.model.Events; // Import Events
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate; // Import RestTemplate

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApprovalServiceImp implements ApprovalService {

    private static final Logger log = LoggerFactory.getLogger(ApprovalServiceImp.class);
    private final ApprovalRepository approvalRepository;
    private final RestTemplate restTemplate; // Inject RestTemplate

    @Override
    public ApprovalResponse createApproval(ApprovalRequest approvalRequest) {
        // Get event from EventService
        Events event = restTemplate.getForObject("http://event-service/api/events/" + approvalRequest.getEventId(), Events.class);
        if (event == null) {
            throw new IllegalArgumentException("Event not found");
        }

        // Check the role of the user requesting the approval
        String userRole = restTemplate.getForObject("http://user-service/api/users/" + approvalRequest.getUserId() + "/role", String.class);
        if (userRole == null || !userRole.equals("STAFF")) {
            throw new SecurityException("Only staff members can approve events.");
        }

        Approval approval = Approval.builder()
                .eventId(approvalRequest.getEventId())
                .userId(approvalRequest.getUserId())
                .approved(approvalRequest.isApproved())
                .comments(approvalRequest.getComments())
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
