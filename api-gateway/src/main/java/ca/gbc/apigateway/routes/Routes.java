package ca.gbc.apigateway.routes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.server.mvc.filter.CircuitBreakerFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;
import org.springframework.web.servlet.function.support.HandlerFunctionAdapter;

import java.net.URI;

import static org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions.setPath;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;

@Configuration
@Slf4j
public class Routes {

    @Value("${service.user-url}")
    private String userServiceUrl;
    @Value("${service.event-url}")
    private String eventServiceUrl;
    @Value("${service.room-url}")
    private String roomServiceUrl;
    @Value("${service.booking-url}")
    private String bookingServiceUrl;
    @Value("${service.approval-url}")
    private String approvalServiceUrl;


    @Bean
    public RouterFunction<ServerResponse> userServiceRoute(HandlerFunctionAdapter handlerFunctionAdapter) {

        log.info("Initializing product-service route with URL {}", userServiceUrl);

        return route("user_service")
                .route(RequestPredicates.path("/api/user"), request -> {

                    log.info("Received request for user-service : {}", request.uri());

                    try {
                        ServerResponse response = HandlerFunctions.http(userServiceUrl).handle(request);
                        log.info("Response status for user-service : {}", response.statusCode());
                        return response;
                    } catch (Exception e) {
                        log.error("Error while handling request for user-service : {}", e.getMessage(), e);
                        return ServerResponse.status(500).body("Error occurred when routing user-service");
                    }
                })
                .filter(CircuitBreakerFilterFunctions
                        .circuitBreaker("userServiceCircuitBreaker", URI.create("forward:/fallbackRoute")))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> eventServiceRoute(HandlerFunctionAdapter handlerFunctionAdapter) {

        log.info("Initializing event-service route with URL {}", eventServiceUrl);

        return route("event_service")
                .route(RequestPredicates.path("/api/event"), request -> {

                    log.info("Received request for event-service : {}", request.uri());

                    try {
                        ServerResponse response = HandlerFunctions.http(eventServiceUrl).handle(request);
                        log.info("Response status for event-service : {}", response.statusCode());
                        return response;
                    } catch (Exception e) {
                        log.error("Error while handling request for event-service : {}", e.getMessage(), e);
                        return ServerResponse.status(500).body("Error occurred when routing event-service");
                    }
                })
                .filter(CircuitBreakerFilterFunctions
                        .circuitBreaker("eventServiceCircuitBreaker", URI.create("forward:/fallbackRoute")))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> userServiceSwaggerRoutes() {

        return route("user_service_swagger")
                .route(RequestPredicates.path("/aggregate/user-service/v3/api-docs"),
                        HandlerFunctions.http(userServiceUrl))
                .filter(setPath("/api/docs"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> eventServiceSwaggerRoutes() {

        return route("event_service_swagger")
                .route(RequestPredicates.path("/aggregate/event-service/v3/api-docs"),
                        HandlerFunctions.http(eventServiceUrl))
                .filter(setPath("/api/docs"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> roomServiceSwaggerRoutes() {

        return route("room_service_swagger")
                .route(RequestPredicates.path("/aggregate/room-service/v3/api-docs"),
                        HandlerFunctions.http(roomServiceUrl))
                .filter(setPath("/api/docs"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> bookingServiceSwaggerRoutes() {

        return route("booking_service_swagger")
                .route(RequestPredicates.path("/aggregate/booking-service/v3/api-docs"),
                        HandlerFunctions.http(bookingServiceUrl))
                .filter(setPath("/api/docs"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> approvalServiceSwaggerRoutes() {

        return route("approval_service_swagger")
                .route(RequestPredicates.path("/aggregate/approval-service/v3/api-docs"),
                        HandlerFunctions.http(approvalServiceUrl))
                .filter(setPath("/api/docs"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> fallbackRoute() {

        return route("fallbackRoute")
                .route(RequestPredicates.all(),
                        request->ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE)
                                .body("Service is temporarily unavailable"))
                .build();
    }
}
