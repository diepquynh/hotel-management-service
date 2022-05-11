package vn.utc.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.utc.userservice.data.dto.UserDetailsDTO;
import vn.utc.userservice.data.dto.UserRegistrationDTO;
import vn.utc.userservice.data.dto.request.UserUpdateRequestDTO;
import vn.utc.userservice.service.UserService;

@RestController
@RequestMapping("/user-service")
@RequiredArgsConstructor
public class UserServiceController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDetailsDTO> getUserDetail(@PathVariable("id") Integer userId) {
        return new ResponseEntity<>(userService.getUser(userId), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDetailsDTO> registerUser(@RequestBody UserRegistrationDTO registrationRequest) {
        return new ResponseEntity<>(userService.registerUser(registrationRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDetailsDTO> updateUser(@PathVariable("id") Integer userId,
                                                     @RequestBody UserUpdateRequestDTO updateRequest) {
        return new ResponseEntity<>(userService.updateUser(userId, updateRequest), HttpStatus.NO_CONTENT);
    }
}
