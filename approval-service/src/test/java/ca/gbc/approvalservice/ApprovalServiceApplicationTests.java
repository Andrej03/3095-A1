package ca.gbc.approvalservice;

import ca.gbc.approvalservice.dto.ApprovalRequest;
import ca.gbc.approvalservice.dto.ApprovalResponse;
import ca.gbc.approvalservice.model.Approval;
import ca.gbc.approvalservice.repository.ApprovalRepository;
import ca.gbc.approvalservice.service.ApprovalServiceImp;
import ca.gbc.eventservice.model.Events;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ApprovalServiceApplicationTests {

    @Mock
    private ApprovalRepository approvalRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ApprovalServiceImp approvalServiceImp;

    private ApprovalRequest approvalRequest;

    @BeforeEach
    void setUp() {
        // Prepare mock data
        approvalRequest = new ApprovalRequest(1L, 1L, true, "Approved");

        // Mock the external service calls
        Events event = new Events(); // Simulate an event object returned from EventService
        when(restTemplate.getForObject("http://event-service/events/" + approvalRequest.getEventId(), Events.class))
                .thenReturn(event);

        when(restTemplate.getForObject("http://user-service/users/" + approvalRequest.getUserId() + "/role", String.class))
                .thenReturn("STAFF");
    }

    @Test
    void createApproval_shouldReturnApprovalResponse() {
        // Prepare a mocked Approval entity to be saved
        Approval approval = new Approval("12345", approvalRequest.getEventId(), approvalRequest.getUserId(),
                approvalRequest.isApproved(), approvalRequest.getApprovalStatus());

        when(approvalRepository.save(Mockito.any(Approval.class))).thenReturn(approval);

        // Call the service method
        ApprovalResponse approvalResponse = approvalServiceImp.createApproval(approvalRequest);

        // Assert the results
        assertNotNull(approvalResponse);
        assertEquals("12345", approvalResponse.getId());  // ID should be a String
        assertEquals(approvalRequest.getEventId(), approvalResponse.getEventId());
        assertEquals(approvalRequest.getUserId(), approvalResponse.getUserId());
        assertTrue(approvalResponse.isApproved());
        assertEquals(approvalRequest.getApprovalStatus(), approvalResponse.getApprovalStatus());

        // Verify interactions with mocks
        verify(restTemplate, times(1)).getForObject("http://event-service/events/" + approvalRequest.getEventId(), Events.class);
        verify(restTemplate, times(1)).getForObject("http://user-service/users/" + approvalRequest.getUserId() + "/role", String.class);
        verify(approvalRepository, times(1)).save(Mockito.any(Approval.class));
    }

    @Test
    void createApproval_shouldThrowExceptionWhenEventNotFound() {
        // Mock EventService to return null (Event not found)
        when(restTemplate.getForObject("http://event-service/events/" + approvalRequest.getEventId(), Events.class))
                .thenReturn(null);

        // Call the service method and assert the exception
        assertThrows(IllegalArgumentException.class, () -> {
            approvalServiceImp.createApproval(approvalRequest);
        });
    }

    @Test
    void createApproval_shouldThrowSecurityExceptionWhenUserIsNotStaff() {
        // Mock UserService to return non-STAFF role
        when(restTemplate.getForObject("http://user-service/users/" + approvalRequest.getUserId() + "/role", String.class))
                .thenReturn("STUDENT");

        // Call the service method and assert the exception
        assertThrows(SecurityException.class, () -> {
            approvalServiceImp.createApproval(approvalRequest);
        });
    }
}
