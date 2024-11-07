package ca.gbc.eventservice;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;

import static org.hamcrest.MatcherAssert.assertThat;

public class EventServiceApplicationTests {

	@Container
	public static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

	@LocalServerPort
	private Integer port;

	static {
		mongoDBContainer.start();
	}

	@BeforeEach
	public void setup() {
		// Set the base URI for all requests
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	@Test
	public void testCreateEvent() {
		String eventPayload = """
            {
                "eventName": "Tech Conference 2024",
                "organizerId": "123",
                "eventType": "Conference",
                "expectedAttendees": 100
            }
            """;

		var responseBodyString = RestAssured.given()
				.contentType("application/json")
				.body(eventPayload)
				.when()
				.post("/api/events")
				.then()
				.log().all()
				.statusCode(201)  // Assert that the status code is 201 (Created)
				.extract()
				.body().asString();

		// Assert the response message
		assertThat(responseBodyString, Matchers.containsString("eventName"));
		assertThat(responseBodyString, Matchers.containsString("Tech Conference 2024"));
	}

	@Test
	public void testCreateEventWithTooManyAttendeesForStudent() {
		// Create a sample event request for a student with too many attendees
		String eventRequestJson = """
            {
                "eventName": "Student Conference",
                "organizerId": "2",
                "eventType": "Conference",
                "expectedAttendees": 15
            }
            """;

		// Send a POST request and check if it throws the appropriate error
		var responseBodyString = RestAssured.given()
				.contentType("application/json")
				.body(eventRequestJson)
				.when()
				.post("/api/events")
				.then()
				.log().all()
				.statusCode(400)  // Should return a bad request
				.extract()
				.body().asString();

		// Assert the response message
		assertThat(responseBodyString, Matchers.containsString("Students cannot organize events with more than 10 attendees."));
	}

	@Test
	public void testGetAllEvents() {
		// Send a GET request to retrieve all events
		var responseBodyString = RestAssured.given()
				.when()
				.get("/api/events")
				.then()
				.log().all()
				.statusCode(200)  // Ensure the request was successful
				.extract()
				.body().asString();

		// Assert that the response body contains at least 2 events in the list
		assertThat(responseBodyString, Matchers.containsString("\"id\":"));
	}

	@Test
	public void testGetEventById() {
		String eventId = "event123";  // Replace with an actual event ID for testing

		var responseBodyString = RestAssured.given()
				.when()
				.get("/api/events/" + eventId)
				.then()
				.log().all()
				.statusCode(200)  // Assert that the status code is 200 (OK)
				.extract()
				.body().asString();

		// Assert the response contains the expected event details
		assertThat(responseBodyString, Matchers.containsString("eventName"));
		assertThat(responseBodyString, Matchers.containsString("Tech Conference 2024"));
	}

	@Test
	public void testDeleteEvent() {
		String eventId = "event123";  // Replace with an actual event ID for testing

		RestAssured.given()
				.when()
				.delete("/api/events/" + eventId)
				.then()
				.log().all()
				.statusCode(204);  // Assert that the status code is 204 (No Content)
	}

	@Test
	void testToFailWhenUserPermissionIsNotGiven() {
		long userId = 2L;

		String createEventJson = """
            {
                "name": "Test Event",
                "organizerId":""" + userId + """
            }
            """;

		var responseBodyString = RestAssured.given()
				.contentType("application/json")
				.body(createEventJson)
				.when()
				.post("/api/events")
				.then()
				.log().all()
				.statusCode(403)  // Forbidden for non-STAFF users
				.extract()
				.body().asString();

		// Assert the response contains a message indicating lack of permission
		assertThat(responseBodyString, Matchers.containsString("Forbidden for non-STAFF users"));
	}


	@AfterEach
	void tearDown() {
		mongoDBContainer.stop();
	}
}
