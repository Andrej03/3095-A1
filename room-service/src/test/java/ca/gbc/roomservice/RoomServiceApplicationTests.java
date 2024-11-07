package ca.gbc.roomservice;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.hamcrest.MatcherAssert.assertThat;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
public class RoomServiceApplicationTests {

	private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15-alpine");

	@LocalServerPort
	private Integer port;

	static {
		postgreSQLContainer.start();
	}

	@BeforeEach
	void setUp() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	@Test
	void testCreateRoom() {
		String roomRequestJson = """
            {
                "id": 1,
                "roomName": "Conference Room",
                "capacity": 10,
                "features": "Projector, Whiteboard",
                "available": true
            }
            """;

		var responseBodyString = RestAssured.given()
				.contentType("application/json")
				.body(roomRequestJson)
				.when()
				.post("/api/rooms")
				.then()
				.log().all()
				.statusCode(201)  // Assert that the status code is 201 (Created)
				.extract()
				.body()
				.asString();

		assertThat(responseBodyString, Matchers.containsString("Conference Room"));
		assertThat(responseBodyString, Matchers.containsString("true"));
	}

	@Test
	void testGetAllRooms() {
		RestAssured.given()
				.when()
				.get("/api/rooms")
				.then()
				.log().all()
				.statusCode(200)  // Ensure the request was successful
				.body("$", Matchers.hasSize(Matchers.greaterThan(0))); // Ensure there are rooms in the list
	}


	@Test
	void testUpdateRoom() {
		String roomRequestJson = """
            {
                "id": 1,
                "roomName": "Updated Room",
                "capacity": 15,
                "features": "Whiteboard, Projector",
                "available": false
            }
            """;

		var response = RestAssured.given()
				.contentType("application/json")
				.body(roomRequestJson)
				.when()
				.put("/api/rooms/1")
				.then()
				.log().all()
				.statusCode(204); // Should return No Content (204)

		String responseBody = response.extract().body().asString();
		assertThat(responseBody, Matchers.equalTo(""));
	}


	@Test
	void testDeleteRoom() {
		Long roomId = 1L;

		RestAssured.given()
				.when()
				.delete("/api/rooms/{roomId}", roomId)
				.then()
				.log().all()
				.statusCode(204); // Should return No Content (204)
	}


	@Test
	void testCheckRoomAvailability() {
		Long roomId = 1L;
		String startTime = "2024-11-06T10:00:00";
		String endTime = "2024-11-06T12:00:00";

		RestAssured.given()
				.queryParam("startTime", startTime)
				.queryParam("endTime", endTime)
				.when()
				.get("/api/rooms/{roomId}/availability", roomId)
				.then()
				.log().all()
				.statusCode(200)  // Ensure the request was successful
				.body(Matchers.is(true)); // Assert room is available
	}

}
