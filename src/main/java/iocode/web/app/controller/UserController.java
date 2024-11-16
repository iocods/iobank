package iocode.web.app.controller;

import iocode.web.app.dto.UserDto;
import iocode.web.app.entity.User;
import iocode.web.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * This class is responsible for handling user-related requests.
 * It provides endpoints for user registration and authentication.
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Registers a new user using the provided user data.
     *
     * @param userDto The user data to be registered.
     * @return A ResponseEntity containing the registered user.
     */
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.registerUser(userDto));
    }

    /**
     * Authenticates a user using the provided user data.
     *
     * @param userDto The user data to be authenticated.
     * @return A ResponseEntity containing the authentication token and the authenticated user.
     */
    @PostMapping("/auth")
    public ResponseEntity<?> authenticateUser(@RequestBody UserDto userDto) {
        var authObject = userService.authenticateUser(userDto);
        var token = (String) authObject.get("token");
        System.out.println("Jwt token: " + token);
        return ResponseEntity.ok()
            .header("Authorization", token)
            .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Authorization")
            .body(authObject.get("user"));
    }
}
