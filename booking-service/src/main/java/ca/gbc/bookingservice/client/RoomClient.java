package ca.gbc.bookingservice.client;

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
public interface RoomClient {

    Logger log = LoggerFactory.getLogger(RoomClient.class);

    @GetExchange( value = "/api/rooms")
    @CircuitBreaker(name = "rooms", fallbackMethod = "fallbackMethod")
    @RequestMapping(method = RequestMethod.GET, value = "/api/rooms/available")
    @Retry(name = "rooms")
    boolean isRoomAvailable(@RequestParam String roomId, @RequestParam String startTime, @RequestParam String endTime);

    default boolean fallbackMethod(String roomId, String startTime, Throwable throwable) {
        log.info("Room with id {}, is not available at {}, because {}", roomId, startTime, throwable.getMessage());
        return false;
    }
}
