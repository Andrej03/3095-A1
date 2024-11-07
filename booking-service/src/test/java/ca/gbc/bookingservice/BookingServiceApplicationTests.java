package ca.gbc.bookingservice;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
@SpringBootTest
@Testcontainers
public class BookingServiceApplicationTests {

    @Container
    public static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    @LocalServerPort
    private Integer port;

    static{
        mongoDBContainer.start();
    }

    @BeforeEach
    public void setup() {
        // Set the base URI for all requests
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    public void testCreateBooking() {
        String bookingPayload = """
                {
                    "roomId": 101,
                    "userId": 1,
                    "startTime": "2024-11-10T09:00:00",
                    "endTime": "2024-11-10T10:00:00"
                }
                """;

        var responseBodyString = RestAssured.given()
                .contentType("application/json")
                .body(bookingPayload)
                .when()
                .post("/bookings")
                .then()
                .log().all()
                .statusCode(201)  // Assert that the status code is 201 (Created)
                .extract()
                .body().asString();

        // Assert the response message
        assertThat(responseBodyString, Matchers.is("Booking successfully created"));
    }

    @Test
    public void testBookingConflict() {
        String conflictingBookingPayload = """
                {
                    "roomId": 101,
                    "userId": 2,
                    "startTime": "2024-11-10T09:00:00",
                    "endTime": "2024-11-10T10:00:00"
                }
                """;

        var responseBodyString = RestAssured.given()
                .contentType("application/json")
                .body(conflictingBookingPayload)
                .when()
                .post("/bookings")
                .then()
                .log().all()
                .statusCode(409)  // Conflict status code for resource conflict
                .extract()
                .body().asString();

        // Assert the response message
        assertThat(responseBodyString, Matchers.is("Room is already booked for the selected time"));
    }

    @Test
    public void testInvalidBookingRequest() {
        String invalidBookingPayload = """
                {
                    "roomId": -1,
                    "userId": 1,
                    "startTime": "2024-11-10T09:00:00",
                    "endTime": "2024-11-10T08:00:00"
                }
                """;

        var responseBodyString = RestAssured.given()
                .contentType("application/json")
                .body(invalidBookingPayload)
                .when()
                .post("/bookings")
                .then()
                .log().all()
                .statusCode(400)  // Bad request for invalid inputs
                .extract()
                .body().asString();

        // Assert the response message
        assertThat(responseBodyString, Matchers.is("Invalid booking request"));
    }

    @Test
    public void testGetBookingById() {
        var responseBodyString = RestAssured.given()
                .when()
                .get("/bookings/1")  // Adjust the endpoint according to your API path
                .then()
                .log().all()
                .statusCode(200)  // Assert that the status code is 200 (OK)
                .extract()
                .body().asString();

        // Assert the response body or any specific field
        assertThat(responseBodyString, Matchers.is("{\"roomId\": 101, \"userId\": 1, \"startTime\": \"2024-11-10T09:00:00\"}"));
    }

    @AfterEach
    void tearDown() {
        mongoDBContainer.stop();
    }
}
