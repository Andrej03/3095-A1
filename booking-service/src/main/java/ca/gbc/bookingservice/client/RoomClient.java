package ca.gbc.bookingservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "room-service", url = "https://localhost:8089")
public interface RoomClient {

    @RequestMapping(method = RequestMethod.GET, value = "/api/rooms/available")
    boolean isRoomAvailable(@RequestParam Long roomId, @RequestParam String startTime, @RequestParam String endTime);
}
