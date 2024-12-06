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
public interface UserClient {

    Logger log = LoggerFactory.getLogger(UserClient.class);

    @GetExchange( value = "/api/users/")
    @CircuitBreaker(name = "users", fallbackMethod = "fallbackMethod")
    @RequestMapping(method = RequestMethod.GET, value = "/api/users/available")
    @Retry(name = "users")
    boolean isUserApproved(@RequestParam Long userId);

    default boolean fallbackMethod(Long userId, Throwable throwable) {
        log.info("User with id {} has not been approved, {}",userId ,throwable.getMessage());
        return false;
    }
}