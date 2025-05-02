package com.digitaltimecapsule.digitaltimecapsule.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserTest {

    @Test
    void testUserDefaultConstructorAndSetters() {
        User user = new User();

        Long id = 1L;
        String email = "test@example.com";
        String password = "securePassword123";

        user.setId(id);
        user.setEmail(email);
        user.setPassword(password);

        assertEquals(id, user.getId(), "User ID should match");
        assertEquals(email, user.getEmail(), "User email should match");
        assertEquals(password, user.getPassword(), "User password should match");
    }

    @Test
    void testUserParameterizedConstructor() {
        String email = "user@example.com";
        String password = "password456";

        User user = new User(email, password);

        assertEquals(email, user.getEmail(), "Email should match constructor input");
        assertEquals(password, user.getPassword(), "Password should match constructor input");
        assertNull(user.getId(), "ID should be null until persisted");
    }
}
