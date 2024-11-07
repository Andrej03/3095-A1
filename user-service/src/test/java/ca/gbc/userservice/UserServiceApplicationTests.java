package ca.gbc.userservice;


import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.shaded.org.hamcrest.Matchers;

import static org.testcontainers.shaded.org.hamcrest.MatcherAssert.assertThat;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserServiceApplicationTests {

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
    void testCreateUser() {
        String createUserJson = """
            {
                "name": "John Doe",
                "email": "john.doe@example.com",
                "role": "STAFF",
                "userType": "STAFF"
            }
            """;

        var responseBodyString = RestAssured.given()
                .contentType("application/json")
                .body(createUserJson)
                .when()
                .post("/api/users")
                .then()
                .log().all()
                .statusCode(201)
                .extract()
                .body().asString();

        assertThat(responseBodyString, Matchers.containsString("John Doe"));
        assertThat(responseBodyString, Matchers.containsString("STAFF"));
    }


    @Test
    void testGetAllUsers() {
        var responseBody = RestAssured.given()
                .contentType("application/json")
                .when()
                .get("/api/users")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .body().asString();

        assertThat(responseBody, Matchers.containsString("John Doe"));
    }


    @Test
    void testGetUserById() {
        // Assuming user with ID 1 exists
        long userId = 1L;

        var responseBody = RestAssured.given()
                .contentType("application/json")
                .when()
                .get("/api/users/" + userId)
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .body().asString();

        assertThat(responseBody, Matchers.containsString("John Doe"));
        assertThat(responseBody, Matchers.containsString("STAFF"));
    }


    @Test
    void testDeleteUser() {
        // Assuming user with ID 1 exists
        long userId = 1L;

        RestAssured.given()
                .when()
                .delete("/api/users/" + userId)
                .then()
                .log().all()
                .statusCode(204);
    }

    @Test
    void testGetUserRole() {
        // Assuming user with ID 1 exists
        long userId = 1L;

        var response = RestAssured.given()
                .contentType("application/json")
                .when()
                .get("/api/users/" + userId + "/role")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .asString();

        assertThat(response, Matchers.containsString("STAFF"));
    }


}
