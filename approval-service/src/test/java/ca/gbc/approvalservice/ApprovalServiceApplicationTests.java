package ca.gbc.approvalservice;

import ca.gbc.approvalservice.dto.ApprovalResponse;
import ca.gbc.approvalservice.service.ApprovalService;
import ca.gbc.approvalservice.model.Approval;
import ca.gbc.approvalservice.repository.ApprovalRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
public class ApprovalServiceApplicationTests {

    @Container
    public static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    @Autowired
    private ApprovalRepository approvalRepository;

    @Autowired
    private ApprovalService approvalService;

    private String approvalId;

    @BeforeEach
    void setUp() {
        mongoDBContainer.start();
        approvalId = approvalService.toString();
    }

    @Test
    void testCreateApproval() {
        Approval approval = new Approval(null, 1L, 1L, true, "Approved");
        approvalRepository.save(approval);

        assertNotNull(approval.getId());
        assertEquals(1L, approval.getEventId());
        assertEquals(1L, approval.getUserId());
        assertTrue(approval.isApproved());

        mongoDBContainer.stop();
    }

    @Test
    void testGetAllApprovals() {
        // Add two more approvals for testing
        approvalRepository.save(new Approval(null, 2L, 2L, true, "Approved"));
        approvalRepository.save(new Approval(null, 3L, 3L, false, "Rejected"));

        // Retrieve all approvals
        List<ApprovalResponse> approvals = approvalRepository.findAll().stream()
                .map(approval -> new ApprovalResponse(
                        approval.getId(),
                        approval.getEventId(),
                        approval.getUserId(),
                        approval.isApproved(),
                        approval.getApprovalStatus()))
                .toList();

        assertNotNull(approvals);
        assertEquals(3, approvals.size());
    }

    @Test
    void testGetApprovalById() {
        ApprovalResponse approvalResponse = approvalRepository.findById(approvalId)
                .map(approval -> new ApprovalResponse(
                        approval.getId(),
                        approval.getEventId(),
                        approval.getUserId(),
                        approval.isApproved(),
                        approval.getApprovalStatus()))
                .orElse(null);

        assertNotNull(approvalResponse);
        assertEquals(1L, approvalResponse.getEventId());
        assertEquals(1L, approvalResponse.getUserId());
        assertTrue(approvalResponse.isApproved());
    }

    @Test
    void testDeleteApproval() {
        // Delete the approval we created earlier
        approvalRepository.deleteById(approvalId);

        // Verify it was deleted
        assertFalse(approvalRepository.existsById(approvalId));
    }

    @AfterEach
    void tearDown() {
        mongoDBContainer.stop();
    }
}
