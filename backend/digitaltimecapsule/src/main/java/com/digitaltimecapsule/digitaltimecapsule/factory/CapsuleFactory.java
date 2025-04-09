package com.digitaltimecapsule.digitaltimecapsule.factory;

import com.digitaltimecapsule.digitaltimecapsule.builder.CapsuleBuilder;
import com.digitaltimecapsule.digitaltimecapsule.model.Capsule;

import java.time.LocalDateTime;

public class CapsuleFactory {

    public static Capsule createTextCapsule(String name, String textContent, LocalDateTime expiry) {
        return new CapsuleBuilder()
                .setName(name)
                .setExpiryDateTime(expiry)
                .addCapsuleData("text", textContent)
                .build();
    }

    public static Capsule createFileCapsule(String name, String filePath, String type, LocalDateTime expiry) {
        return new CapsuleBuilder()
                .setName(name)
                .setExpiryDateTime(expiry)
                .addCapsuleData(type, filePath)
                .build();
    }

    public static CapsuleBuilder getBuilder() {
        return new CapsuleBuilder();
    }
}
