package ca.gbc.eventservice.client;

import groovy.util.logging.Slf4j;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public interface BookingClient {

    Logger log = LoggerFactory.getLogger(BookingClient.class);

    @GetExchange(value = "/api/bookings")
    @CircuitBreaker(name = "booking", fallbackMethod = "fallbackMethod")
    @Retry(name = "booking")
    String getEventBooked(@PathVariable String eventId, @PathVariable String eventName);

    default String fallbackMethod(String eventId, String eventName, Throwable throwable) {
        log.info("Unable to get booking info for event {} with ID {}. Reason: {}",
                eventName, eventId, throwable.getMessage());
        return "Fallback booking info: Unavailable";
    }
}
