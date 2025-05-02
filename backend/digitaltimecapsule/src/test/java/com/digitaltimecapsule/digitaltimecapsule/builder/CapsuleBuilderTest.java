package com.digitaltimecapsule.digitaltimecapsule.builder;

import com.digitaltimecapsule.digitaltimecapsule.model.Capsule;
import com.digitaltimecapsule.digitaltimecapsule.model.CapsuleData;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CapsuleBuilderTest {

    @Test
    void testCapsuleBuilderCreatesCorrectCapsule() {
        String name = "Test Capsule";
        LocalDateTime expiry = LocalDateTime.now().plusDays(30);
        boolean opened = true;

        Capsule capsule = new CapsuleBuilder()
                .setName(name)
                .setExpiryDateTime(expiry)
                .setOpened(opened)
                .addCapsuleData("text", "This is a test message.")
                .addCapsuleData("image", "image_data_base64")
                .build();

        assertNotNull(capsule, "Capsule should not be null");
        assertEquals(name, capsule.getName(), "Capsule name should match");
        assertEquals(expiry, capsule.getExpiryDateTime(), "Expiry date should match");
        assertEquals(opened, capsule.isOpened(), "Opened status should match");

        List<CapsuleData> dataList = capsule.getCapsuleDataList();
        assertEquals(2, dataList.size(), "There should be 2 capsule data entries");

        CapsuleData first = dataList.get(0);
        CapsuleData second = dataList.get(1);

        assertEquals("text", first.getDataType(), "First data type should be 'text'");
        assertEquals("This is a test message.", first.getContent(), "First content should match");
        assertSame(capsule, first.getCapsule(), "First data should reference the capsule");

        assertEquals("image", second.getDataType(), "Second data type should be 'image'");
        assertEquals("image_data_base64", second.getContent(), "Second content should match");
        assertSame(capsule, second.getCapsule(), "Second data should reference the capsule");
    }
}
