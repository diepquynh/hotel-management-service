package vn.utc.hotelmanager.auth.user.controller;

import vn.utc.hotelmanager.auth.exception.UserAlreadyExistException;
import vn.utc.hotelmanager.auth.user.data.UserRepository;
import vn.utc.hotelmanager.auth.user.data.dto.UserRegistrationDTO;
import vn.utc.hotelmanager.auth.user.model.User;
import vn.utc.hotelmanager.auth.user.service.ApplicationUserService;
import vn.utc.hotelmanager.auth.user.service.OAuth2UserServiceImpl;
import vn.utc.hotelmanager.auth.user.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping("/users")
public class UserController {
    private final OAuth2UserServiceImpl oAuth2UserService;
    private final UserDetailsServiceImpl userDetailsService;
    private final UserRepository userDAO;

    @Autowired
    public UserController(OAuth2UserServiceImpl oAuth2UserService, UserDetailsServiceImpl userDetailsService, UserRepository userDAO) {
        this.oAuth2UserService = oAuth2UserService;
        this.userDetailsService = userDetailsService;
        this.userDAO = userDAO;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public ResponseEntity getUserInfo() {
        String username = ApplicationUserService.getCurrentUser().getUsername();
        User user = userDAO.findByUsername(username);

        return ResponseEntity.ok().body(user);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity registerUser(@RequestBody UserRegistrationDTO userRequest) {
        try {
            userDetailsService.registerUser(userRequest);
        } catch (UserAlreadyExistException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("OK");
    }

    @RequestMapping(value = "/login/oauth", method = RequestMethod.POST)
    public ResponseEntity oAuthLogin(@RequestParam("token") String idToken)
            throws GeneralSecurityException, IOException {
        String newToken = oAuth2UserService.processToken(idToken);

        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + newToken)
                .body(null);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseEntity updateUser(@RequestBody User newUser) {
        String username = ApplicationUserService.getCurrentUser().getUsername();
        User loggedUser = userDAO.findByUsername(username);
        if (loggedUser.getId() != newUser.getId())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Request forbidden");

        String newToken;
        try {
            newToken = userDetailsService.updateUser(newUser);
        } catch (UserAlreadyExistException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .header("Authorization", "Bearer " + newToken)
                .body("OK");
    }
}
