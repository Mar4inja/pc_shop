package de.ait.pcshop.controller;

import de.ait.pcshop.model.User;
import de.ait.pcshop.repository.UserRepository;
import de.ait.pcshop.service.interfaces.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;


    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        User createdUser = userService.addUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @DeleteMapping("/auth/me/delete")
    public ResponseEntity<ErrorResponse> deleteUser(Authentication authentication, HttpServletResponse response) {
        userService.deleteUser(authentication);
        Cookie cookie = new Cookie("Access-Token", null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
}

    @PostMapping("/auth/me/disable")
    public ResponseEntity<User> disableUser(Authentication authentication) {
   return ResponseEntity.ok(userService.disableUser(authentication));
    }

    @PostMapping("/auth/me/enable")
    public ResponseEntity<User> enableUser(Authentication authentication) {
        return ResponseEntity.ok(userService.enableUser(authentication));
    }
}