package ca.gbc.approvalservice;

import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@Testcontainers
public class ApprovalServiceApplicationTests {

    @Container
    public static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    @LocalServerPort
    private Integer port;

    static {
        mongoDBContainer.start();
    }

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    void testCreateApproval() {
        String createApprovalJson = """
                {
                    "eventId": 1,
                    "userId": 1,
                    "approved": true,
                    "approvalStatus": "Approved"
                }
                """;

        var responseBodyString = RestAssured.given()
                .contentType("application/json")
                .body(createApprovalJson)
                .when()
                .post("/api/approvals")
                .then()
                .log().all()
                .statusCode(201)  // Created status
                .extract()
                .body().asString();

        assertThat(responseBodyString, containsString("Approved"));

        RestAssured.given()
                .contentType("application/json")
                .when()
                .get("/api/approvals")
                .then()
                .log().all()
                .statusCode(200)
                .body(containsString("Approved"));
    }

    @Test
    void testGetAllApprovals() {
        String createApprovalJson1 = """
                {
                    "eventId": 2,
                    "userId": 2,
                    "approved": true,
                    "approvalStatus": "Approved"
                }
                """;

        String createApprovalJson2 = """
                {
                    "eventId": 3,
                    "userId": 3,
                    "approved": false,
                    "approvalStatus": "Rejected"
                }
                """;

        RestAssured.given()
                .contentType("application/json")
                .body(createApprovalJson1)
                .when()
                .post("/api/approvals")
                .then()
                .statusCode(201);

        RestAssured.given()
                .contentType("application/json")
                .body(createApprovalJson2)
                .when()
                .post("/api/approvals")
                .then()
                .statusCode(201); // CREATED

        var responseBody = RestAssured.given()
                .contentType("application/json")
                .when()
                .get("/api/approvals")
                .then()
                .log().all()
                .statusCode(200) // OK
                .extract()
                .body().asString();

        assertThat(responseBody, containsString("Approved"));
        assertThat(responseBody, containsString("Rejected"));
    }

    @Test
    void testGetApprovalById() {
        // Assuming approvalId 1 exists for testing
        long approvalId = 1L;

        var responseBodyString = RestAssured.given()
                .contentType("application/json")
                .when()
                .get("/api/approvals/" + approvalId)
                .then()
                .log().all()
                .statusCode(200) // OK
                .extract()
                .body().asString();

        // Verifying the response body
        assertThat(responseBodyString, containsString("Approved"));

        RestAssured.given()
                .contentType("application/json")
                .when()
                .get("/api/approvals/" + approvalId)
                .then()
                .log().all()
                .statusCode(200) // OK
                .body(containsString("Approved"));
    }

    @Test
    void testDeleteApproval() {
        long approvalId = 1L;

        RestAssured.given()
                .when()
                .delete("/api/approvals/" + approvalId)
                .then()
                .log().all()
                .statusCode(204); // NO CONTENT to delete

        // Verify it was deleted by checking its absence
        RestAssured.given()
                .contentType("application/json")
                .when()
                .get("/api/approvals/" + approvalId)
                .then()
                .log().all()
                .statusCode(404);
    }

    @Test
    void testFailWhenNonStaffApprovesEvent() {
        long userId = 2L;
        long eventId = 1L;

        var responseBodyString = RestAssured.given()
                .contentType("application/json")
                .when()
                .post("/api/approvals/" + eventId + "/approve?userId=" + userId)
                .then()
                .log().all()
                .statusCode(403)
                .extract()
                .body().asString();

        assertThat(responseBodyString, containsString("Forbidden"));
    }

    @AfterEach
    void tearDown() {
        mongoDBContainer.stop();
    }
}
