package ca.gbc.roomservice;

import ca.gbc.roomservice.dto.RoomRequest;
import io.restassured.RestAssured;
import org.hamcrest.Matchers; // This is the correct import for Matchers
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.hamcrest.MatcherAssert.assertThat; // Ensure this is also imported from org.hamcrest

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class RoomServiceApplicationTests {

	@LocalServerPort
	private Integer port;

	@BeforeEach
	void setUp() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	@Test
	void shouldCreateRoom() {
		RoomRequest roomRequest = new RoomRequest(
				1L, "Conference Room", 10, "Projector, Whiteboard", true
		);

		var responseBodyString = RestAssured.given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(roomRequest)
				.when()
				.post("/api/rooms")
				.then()
				.log().all()
				.statusCode(HttpStatus.CREATED.value())
				.extract()
				.body()
				.asString();

		assertThat(responseBodyString, Matchers.containsString("Conference Room"));
		assertThat(responseBodyString, Matchers.containsString("true"));
	}

	@Test
	void shouldGetAllRooms() {
		RestAssured.given()
				.when()
				.get("/api/rooms")
				.then()
				.log().all()
				.statusCode(HttpStatus.OK.value());
	}

	@Test
	void shouldUpdateRoom() {
		RoomRequest roomRequest = new RoomRequest(
				1L, "Updated Room", 15, "Whiteboard, Projector", false
		);
		var response = RestAssured.given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(roomRequest)
				.when()
				.put("/api/rooms/1")
				.then()
				.log().all()
				.statusCode(HttpStatus.NO_CONTENT.value());
		String responseBody = response.extract().body().asString();
		assertThat(responseBody, Matchers.equalTo(""));
	}

	@Test
	void shouldDeleteRoom() {
		Long roomId = 1L;

		RestAssured.given()
				.when()
				.delete("/api/rooms/{roomId}", roomId)
				.then()
				.log().all()
				.statusCode(HttpStatus.NO_CONTENT.value());
	}

	@Test
	void shouldCheckRoomAvailability() {
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
				.statusCode(HttpStatus.OK.value())
				.body(Matchers.is(true)); // Ensure you are using the correct Matcher here
	}
}
