package com.digitaltimecapsule.digitaltimecapsule.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.digitaltimecapsule.digitaltimecapsule.model.User;
import com.digitaltimecapsule.digitaltimecapsule.service.UserService;
import com.digitaltimecapsule.digitaltimecapsule.security.JwtUtil;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }


    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            if (jwtUtil.validateToken(token)) {
                return ResponseEntity.ok().body("Token is valid");
            } else {
                return ResponseEntity.status(401).body("Invalid token");
            }
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid or expired token");
        }
    }


    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User req) {
        try {
            User user = userService.registerUser(req);
            return ResponseEntity.ok("User registered succesfully with ID: " + user.getId());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User req) {
        try {
            User user = userService.loginUser(req);
            String token = jwtUtil.generateToken(user.getEmail());
            return ResponseEntity.ok().body(token);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
