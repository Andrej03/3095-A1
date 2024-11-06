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
        // Get event from EventService using RestTemplate
        Events event = restTemplate.getForObject("http://event-service/events/" + approvalRequest.getEventId(), Events.class);

        if (event == null) {
            throw new IllegalArgumentException("Event not found");
        }

        // Check if user is a staff member
        String userRole = restTemplate.getForObject("http://user-service/users/" + approvalRequest.getUserId() + "/role", String.class);
        if (!"STAFF".equals(userRole)) {
            throw new SecurityException("Only staff members can approve events.");
        }

        // Create approval
        Approval approval = new Approval();
        approval.setEventId(approvalRequest.getEventId());
        approval.setUserId(approvalRequest.getUserId());
        approval.setApproved(approvalRequest.isApproved());
        approval.setApprovalStatus(approvalRequest.getApprovalStatus());

        // Save approval and return response
        approval = approvalRepository.save(approval);

        return new ApprovalResponse(approval.getId(), approval.getEventId(), approval.getUserId(), approval.isApproved(), approval.getApprovalStatus());
    }

    @Override
    public ApprovalResponse getApprovalById(Long approvalId) {
        return approvalRepository.findById(approvalId)
                .map(approval -> new ApprovalResponse(approval.getId(), approval.getEventId(), approval.getUserId(), approval.isApproved(), approval.getApprovalStatus()))
                .orElse(null);
    }

    @Override
    public void deleteApproval(Long approvalId) {
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
