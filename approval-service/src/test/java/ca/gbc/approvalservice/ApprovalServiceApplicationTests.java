package ca.gbc.approvalservice;

import ca.gbc.approvalservice.model.Approval;
import ca.gbc.approvalservice.repository.ApprovalRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
public class ApprovalServiceApplicationTests {

    @Container
    public static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    @Autowired
    private ApprovalRepository approvalRepository;

    @Test
    void testCreateApproval() {
        mongoDBContainer.start();

        Approval approval = new Approval(null, 1L, 1L, true, "Approved");
        approvalRepository.save(approval);

        assertNotNull(approval.getId());
        assertEquals(1L, approval.getEventId());
        assertEquals(1L, approval.getUserId());
        assertTrue(approval.isApproved());

        mongoDBContainer.stop();
    }
}
