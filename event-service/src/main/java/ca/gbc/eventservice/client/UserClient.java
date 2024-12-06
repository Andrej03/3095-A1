package ca.gbc.eventservice.client;

import groovy.util.logging.Slf4j;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public interface UserClient {

    Logger log = LoggerFactory.getLogger(UserClient.class);

    @GetExchange(value = "/api/user/{userId}/role")
    @CircuitBreaker(name = "user", fallbackMethod = "fallbackMethod")
    @Retry(name = "user")
    String getUserRole(@PathVariable("userId") Long userId);

    default boolean fallbackMethod(String eventName, Throwable throwable) {
        log.info("Event {} , information can not be given {}", eventName, throwable.getMessage());
        return false;
    }
}
