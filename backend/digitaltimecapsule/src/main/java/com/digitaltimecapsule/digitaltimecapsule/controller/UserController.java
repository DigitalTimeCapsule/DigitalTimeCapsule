package com.digitaltimecapsule.digitaltimecapsule.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.digitaltimecapsule.digitaltimecapsule.model.User;
import com.digitaltimecapsule.digitaltimecapsule.service.UserService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
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
    public ResponseEntity<String> loginUser(@RequestBody User req) {
        try{
            User user = userService.loginUser(req);
            return ResponseEntity.ok("User logged in succesfully with ID: " + user.getId());
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
