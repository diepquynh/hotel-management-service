package vn.utc.clients.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "user",
        url = "${clients.user.url}"
)
public interface UserClient {

    @PostMapping(path = "api/v1/users")
    UserResponse getUserByUsername(@RequestBody UserRequest userRequest);

    @GetMapping(path = "api/v1/users/{id}")
    UserResponse getUserById(@PathVariable("id") Integer userId);
}
