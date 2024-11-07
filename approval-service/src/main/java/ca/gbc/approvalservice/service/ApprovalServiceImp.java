package ca.gbc.approvalservice.service;

import ca.gbc.approvalservice.dto.ApprovalRequest;
import ca.gbc.approvalservice.dto.ApprovalResponse;
import ca.gbc.approvalservice.model.Approval;
import ca.gbc.approvalservice.repository.ApprovalRepository;
import ca.gbc.eventservice.model.Events;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApprovalServiceImp implements ApprovalService {

    private final ApprovalRepository approvalRepository;
    private final RestTemplate restTemplate;

    @Override
    @Transactional
    public ApprovalResponse createApproval(ApprovalRequest approvalRequest) {
        // Validate if event exists
        Events event = restTemplate.getForObject("http://event-service/events/" + approvalRequest.getEventId(), Events.class);
        if (event == null) {
            throw new IllegalArgumentException("Event not found");
        }

        // Validate if user is staff
        String role = restTemplate.getForObject("http://user-service/users/" + approvalRequest.getUserId() + "/role", String.class);
        if (!"STAFF".equals(role)) {
            throw new SecurityException("User is not authorized");
        }

        // Create the Approval entity
        Approval approval = new Approval("generated-id", approvalRequest.getEventId(),
                approvalRequest.getUserId(), approvalRequest.isApproved(),
                approvalRequest.getApprovalStatus());

        // Save to the repository
        approvalRepository.save(approval);

        // Return the response
        return new ApprovalResponse(approval.getId(), approval.getEventId(), approval.getUserId(),
                approval.isApproved(), approval.getApprovalStatus());
    }



    @Override
    public ApprovalResponse getApprovalById(String approvalId) {
        return approvalRepository.findById(approvalId)
                .map(approval -> new ApprovalResponse(approval.getId(), approval.getEventId(), approval.getUserId(), approval.isApproved(), approval.getApprovalStatus()))
                .orElse(null);
    }

    @Override
    public void deleteApproval(String approvalId) {
        approvalRepository.deleteById(approvalId);
    }

    @Override
    public List<ApprovalResponse> getAllApprovals() {
        List<Approval> approvals = approvalRepository.findAll();
        return approvals.stream()
                .map(approval -> new ApprovalResponse(approval.getId(), approval.getEventId(), approval.getUserId(), approval.isApproved(), approval.getApprovalStatus()))
                .collect(Collectors.toList());
    }
}
