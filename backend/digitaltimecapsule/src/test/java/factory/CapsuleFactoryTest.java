package com.digitaltimecapsule.digitaltimecapsule.factory;

import com.digitaltimecapsule.digitaltimecapsule.builder.CapsuleBuilder;
import com.digitaltimecapsule.digitaltimecapsule.model.Capsule;
import com.digitaltimecapsule.digitaltimecapsule.model.CapsuleData;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CapsuleFactoryTest {

    @Test
    void testCreateTextCapsule() {
        String name = "Test Capsule";
        String content = "This is a test message.";
        LocalDateTime expiry = LocalDateTime.now().plusDays(7);

        Capsule capsule = CapsuleFactory.createTextCapsule(name, content, expiry);

        assertNotNull(capsule, "Capsule should not be null");
        assertEquals(name, capsule.getName(), "Capsule name should match");
        assertEquals(expiry, capsule.getExpiryDateTime(), "Expiry date should match");
        assertFalse(capsule.isOpened(), "Capsule should be unopened by default");

        List<CapsuleData> dataList = capsule.getCapsuleDataList();
        assertEquals(1, dataList.size(), "Should have one data item");
        assertEquals("text", dataList.get(0).getDataType(), "Data type should be text");
        assertEquals(content, dataList.get(0).getContent(), "Text content should match");
    }

    @Test
    void testCreateFileCapsule() {
        String name = "File Capsule";
        String filePath = "/path/to/file.jpg";
        String type = "image";
        LocalDateTime expiry = LocalDateTime.now().plusDays(10);

        Capsule capsule = CapsuleFactory.createFileCapsule(name, filePath, type, expiry);

        assertNotNull(capsule, "Capsule should not be null");
        assertEquals(name, capsule.getName(), "Capsule name should match");
        assertEquals(expiry, capsule.getExpiryDateTime(), "Expiry date should match");

        List<CapsuleData> dataList = capsule.getCapsuleDataList();
        assertEquals(1, dataList.size(), "Should have one file entry");
        assertEquals(type, dataList.get(0).getDataType(), "Data type should match input type");
        assertEquals(filePath, dataList.get(0).getContent(), "File path content should match");
    }

    @Test
    void testGetBuilderReturnsNewInstance() {
        CapsuleBuilder builder1 = CapsuleFactory.getBuilder();
        CapsuleBuilder builder2 = CapsuleFactory.getBuilder();

        assertNotNull(builder1, "Builder instance should not be null");
        assertNotNull(builder2, "Second builder instance should not be null");
        assertNotSame(builder1, builder2, "Each call to getBuilder should return a new instance");
    }
}
