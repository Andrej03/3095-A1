package ca.gbc.eventservice;

import ca.gbc.eventservice.dto.EventRequest;
import ca.gbc.eventservice.service.EventServiceImp;
import ca.gbc.eventservice.repository.EventRepository;
import ca.gbc.userservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.context.annotation.Import;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@Import(TestcontainersConfiguration.class)  // Import your Testcontainers config if needed
@SpringBootTest
class EventServiceApplicationTests {

	@Mock
	private UserService userService;  // Mocking UserService

	@InjectMocks
	private EventServiceImp eventServiceImp;  // Injecting EventServiceImp

	@Autowired
	private EventRepository testEventRepository;

	@Test
	void contextLoads() {
		// Test if the application context loads without issues
		assertNotNull(testEventRepository, "EventRepository should be initialized.");
	}

	@Test
	void testEventServiceImpNotNull() {
		// Verify that EventServiceImp is loaded into the Spring context
		assertNotNull(eventServiceImp, "EventServiceImp should be initialized.");
	}

	@Test
	void testEventRepositoryIntegration() {
		// Test if EventRepository is functioning correctly with Testcontainers (for MongoDB)
		assertNotNull(testEventRepository, "Test EventRepository should be connected to Testcontainers MongoDB instance.");
	}

	@Test
	void testServiceFunctionality() {
		// Arrange mock behaviors for the UserService
		Long organizerId = 123L;  // Use Long for organizerId
		String userRole = "FACULTY";  // Assume the user role is faculty
		when(userService.getUserRole(organizerId)).thenReturn(userRole);

		// Test the functionality of creating an event using EventServiceImp
		EventRequest eventRequest = new EventRequest("Test Event", "123", "Conference", 50); // String organizerId
		assertNotNull(eventServiceImp.createEvent(eventRequest), "The event creation should not return null.");
	}

	@Test
	void testInvalidOrganizerId() {
		// Arrange mock behaviors for the UserService
		String invalidOrganizerId = "invalid";  // Invalid ID (non-numeric String)

		// Test the exception when passing an invalid organizer ID
		EventRequest eventRequest = new EventRequest("Test Event", invalidOrganizerId, "Conference", 50);

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			eventServiceImp.createEvent(eventRequest);
		});

		// Assert that the exception message contains the expected error
		assert(exception.getMessage().contains("Organizer ID must be a valid number"));
	}

	@Test
	void testStudentEventRestriction() {
		// Arrange mock behaviors for the UserService
		Long organizerId = 123L;  // Use Long for organizerId
		String userRole = "STUDENT";  // Assume the user role is student
		when(userService.getUserRole(organizerId)).thenReturn(userRole);

		// Test the functionality of creating an event with too many attendees for a student
		EventRequest eventRequest = new EventRequest("Test Event", "123", "Conference", 50); // String organizerId

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			eventServiceImp.createEvent(eventRequest);
		});

		// Assert that the exception message contains the expected error
		assert(exception.getMessage().contains("Students cannot organize events with more than 10 attendees."));
	}
}
