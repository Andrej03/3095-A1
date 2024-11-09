package ca.gbc.eventservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "user-service", url="${https://localhost:5443}")
public interface UserClient {
    @GetMapping("/api/user/{userId}/role")
    String getUserRole(@PathVariable("userId") Long userId);
}
