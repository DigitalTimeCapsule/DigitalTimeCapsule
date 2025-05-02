package com.digitaltimecapsule.digitaltimecapsule.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CapsuleTest {

    @Test
    void testCapsuleSettersAndGetters() {
        Capsule capsule = new Capsule();

        Long id = 1L;
        String name = "Test Capsule";
        LocalDateTime expiry = LocalDateTime.now().plusDays(10);
        boolean opened = true;

        capsule.setId(id);
        capsule.setName(name);
        capsule.setExpiryDateTime(expiry);
        capsule.setOpened(opened);

        assertEquals(id, capsule.getId(), "ID should match");
        assertEquals(name, capsule.getName(), "Name should match");
        assertEquals(expiry, capsule.getExpiryDateTime(), "Expiry date should match");
        assertTrue(capsule.isOpened(), "Opened flag should be true");
    }

    @Test
    void testCapsuleDataListHandling() {
        Capsule capsule = new Capsule();
        CapsuleData data1 = new CapsuleData();
        CapsuleData data2 = new CapsuleData();

        capsule.setCapsuleDataList(List.of(data1, data2));

        List<CapsuleData> dataList = capsule.getCapsuleDataList();
        assertEquals(2, dataList.size(), "Capsule should contain 2 data entries");
        assertTrue(dataList.contains(data1), "CapsuleData list should contain data1");
        assertTrue(dataList.contains(data2), "CapsuleData list should contain data2");
    }
}
