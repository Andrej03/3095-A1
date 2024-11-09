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
				.statusCode(201)  // CREATED
				.extract()
				.body().asString();

		assertThat(responseBodyString, Matchers.is("eventName"));
		assertThat(responseBodyString, Matchers.is("Tech Conference 2024"));
	}

	@Test
	public void testCreateEventWithTooManyAttendeesForStudent() {
		String eventRequestJson = """
            {
                "eventName": "Student Conference",
                "organizerId": "2",
                "eventType": "Conference",
                "expectedAttendees": 15
            }
            """;

		var responseBodyString = RestAssured.given()
				.contentType("application/json")
				.body(eventRequestJson)
				.when()
				.post("/api/events")
				.then()
				.log().all()
				.statusCode(400)  // BAD REQUEST
				.extract()
				.body().asString();

		// Assert the response message
		assertThat(responseBodyString, Matchers.is("Students cannot organize events with more than 10 attendees."));
	}

	@Test
	public void testGetAllEvents() {
		var responseBodyString = RestAssured.given()
				.when()
				.get("/api/events")
				.then()
				.log().all()
				.statusCode(200)  // OK
				.extract()
				.body().asString();

		assertThat(responseBodyString, Matchers.is("\"id\":"));
	}

	@Test
	public void testGetEventById() {
		String eventId = "event123";

		var responseBodyString = RestAssured.given()
				.when()
				.get("/api/events/" + eventId)
				.then()
				.log().all()
				.statusCode(200)  // OK
				.extract()
				.body().asString();

		assertThat(responseBodyString, Matchers.is("eventName"));
		assertThat(responseBodyString, Matchers.is("Tech Conference 2024"));
	}

	@Test
	public void testDeleteEvent() {
		String eventId = "event123";

		RestAssured.given()
				.when()
				.delete("/api/events/" + eventId)
				.then()
				.log().all()
				.statusCode(204);
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
				.statusCode(403)
				.extract()
				.body().asString();

		assertThat(responseBodyString, Matchers.is("Forbidden for non-STAFF users"));
	}


	@AfterEach
	void tearDown() {
		mongoDBContainer.stop();
	}
}
