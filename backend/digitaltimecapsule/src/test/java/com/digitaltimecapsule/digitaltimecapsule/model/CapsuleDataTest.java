package com.digitaltimecapsule.digitaltimecapsule.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CapsuleDataTest {

    @Test
    void testCapsuleDataSettersAndGetters() {
        CapsuleData capsuleData = new CapsuleData();

        Long id = 10L;
        String dataType = "text";
        String content = "This is a sample message";

        Capsule capsule = new Capsule();
        capsule.setId(1L);

        capsuleData.setId(id);
        capsuleData.setDataType(dataType);
        capsuleData.setContent(content);
        capsuleData.setCapsule(capsule);

        assertEquals(id, capsuleData.getId(), "ID should match");
        assertEquals(dataType, capsuleData.getDataType(), "Data type should match");
        assertEquals(content, capsuleData.getContent(), "Content should match");
        assertEquals(capsule, capsuleData.getCapsule(), "Capsule reference should match");
    }
}
