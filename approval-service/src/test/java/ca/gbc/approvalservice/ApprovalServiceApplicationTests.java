package ca.gbc.approvalservice;

import ca.gbc.approvalservice.dto.ApprovalRequest;
import ca.gbc.approvalservice.dto.ApprovalResponse;
import ca.gbc.approvalservice.model.Approval;
import ca.gbc.approvalservice.repository.ApprovalRepository;
import ca.gbc.approvalservice.service.ApprovalServiceImp;
import ca.gbc.eventservice.model.Events;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ApprovalServiceTest {

    @InjectMocks
    private ApprovalServiceImp approvalService;

    @Mock
    private ApprovalRepository approvalRepository;

    @Mock
    private RestTemplate restTemplate;

    private ApprovalRequest approvalRequest;
    private Approval approval;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Correct constructor for ApprovalRequest and Approval
        approvalRequest = new ApprovalRequest(1L, 1L, true, "Approved");
        approval = new Approval(1L, 1L, 1L, true, "Approved");
    }

    @Test
    void testCreateApproval_Success() {
        // Create an Events instance using the correct constructor
        Events event = new Events(1L, "Sample Event", 1L, "Conference", 50);

        // Mock the behavior of RestTemplate to return the event
        when(restTemplate.getForObject(anyString(), eq(Events.class))).thenReturn(event);

        // Mock that the user has the role "STAFF"
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn("STAFF");

        // Mock the save behavior of the repository
        when(approvalRepository.save(any(Approval.class))).thenReturn(approval);

        // Call the service method
        ApprovalResponse approvalResponse = approvalService.createApproval(approvalRequest);

        // Assert the expected behavior
        assertNotNull(approvalResponse);
        assertEquals(approvalRequest.getEventId(), approvalResponse.getEventId());
        assertTrue(approvalResponse.isApproved());
        verify(approvalRepository, times(1)).save(any(Approval.class));
    }

    @Test
    void testCreateApproval_Failure_EventNotFound() {
        // Mock the behavior of RestTemplate to return null (Event not found)
        when(restTemplate.getForObject(anyString(), eq(Events.class))).thenReturn(null);

        // Call the service method and assert exception
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            approvalService.createApproval(approvalRequest);
        });

        assertEquals("Event not found", exception.getMessage());
    }

    @Test
    void testCreateApproval_Failure_UserNotStaff() {
        // Mock the behavior of RestTemplate to return a non-STAFF user role
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn("STUDENT");

        // Call the service method and assert exception
        Exception exception = assertThrows(SecurityException.class, () -> {
            approvalService.createApproval(approvalRequest);
        });

        assertEquals("Only staff members can approve events.", exception.getMessage());
    }

    @Test
    void testGetAllApprovals() {
        // Mock the approvalRepository to return a list of approvals
        when(approvalRepository.findAll()).thenReturn(Collections.singletonList(approval));

        // Call the service method
        List<ApprovalResponse> approvalResponses = approvalService.getAllApprovals();

        // Assert the expected behavior
        assertNotNull(approvalResponses);
        assertEquals(1, approvalResponses.size());
        assertEquals(approval.getId(), approvalResponses.getFirst().getId());
    }

    @Test
    void testGetApprovalById_Success() {
        // Mock the approvalRepository to return an approval
        when(approvalRepository.findById(1L)).thenReturn(java.util.Optional.of(approval));

        // Call the service method
        ApprovalResponse approvalResponse = approvalService.getApprovalById(1L);

        // Assert the expected behavior
        assertNotNull(approvalResponse);
        assertEquals(approval.getId(), approvalResponse.getId());
    }

    @Test
    void testGetApprovalById_NotFound() {
        // Mock the approvalRepository to return empty
        when(approvalRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        // Call the service method
        ApprovalResponse approvalResponse = approvalService.getApprovalById(1L);

        // Assert the expected behavior
        assertNull(approvalResponse);
    }

    @Test
    void testDeleteApproval_Success() {
        // Mock the delete behavior
        doNothing().when(approvalRepository).deleteById(1L);

        // Call the service method
        approvalService.deleteApproval(1L);

        // Verify that deleteById was called once
        verify(approvalRepository, times(1)).deleteById(1L);
    }
}
