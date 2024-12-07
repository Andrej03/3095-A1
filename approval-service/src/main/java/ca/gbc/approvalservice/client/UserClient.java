package ca.gbc.approvalservice.client;

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
    @CircuitBreaker(name = "users", fallbackMethod = "fallbackMethod")
    @Retry(name = "users")
    boolean isUserApproved(@PathVariable("userId") Long userId);

    default boolean fallbackMethod(Long userId, Throwable throwable) {
        log.info("User with id {} could not be verified. Reason: {}", userId, throwable.getMessage());
        return false;
    }
}
