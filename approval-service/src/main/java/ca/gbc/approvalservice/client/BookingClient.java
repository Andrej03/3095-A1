package ca.gbc.approvalservice.client;

import groovy.util.logging.Slf4j;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public interface BookingClient {

    Logger log = LoggerFactory.getLogger(BookingClient.class);

    @GetExchange( value = "/api/bookings")
    @CircuitBreaker(name = "booking", fallbackMethod = "fallbackMethod")
    @RequestMapping(method = RequestMethod.GET, value = "/api/bookings/available")
    @Retry(name = "booking")
    boolean isRoomAvailable(@RequestParam Long eventId, @RequestParam Long userId);

    default boolean fallbackMethod(Long eventId, Long userId, Throwable throwable) {
        log.info("Booking for event {}, for user {}, is not approved {}", eventId, userId, throwable.getMessage());
        return false;
    }
}