package com.digitaltimecapsule.digitaltimecapsule.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.digitaltimecapsule.digitaltimecapsule.model.User;
import com.digitaltimecapsule.digitaltimecapsule.repository.UserRepository;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User registerUser(User user) {
        if(userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists with this email");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User loginUser(User user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if(existingUser.isEmpty())
        {
            throw new RuntimeException("Email or Password is incorrect");
        }

        User foundUser = existingUser.get();

        if(!passwordEncoder.matches(user.getPassword(), foundUser.getPassword())){
            throw new RuntimeException("Email or Password is incorrect");
        }

        return foundUser;
    }
}
