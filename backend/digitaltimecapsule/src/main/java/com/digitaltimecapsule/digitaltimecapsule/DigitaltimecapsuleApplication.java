package com.digitaltimecapsule.digitaltimecapsule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.digitaltimecapsule.digitaltimecapsule.repository")

public class DigitaltimecapsuleApplication {
    public static void main(String[] args) {
        SpringApplication.run(DigitaltimecapsuleApplication.class, args);
    }
}
