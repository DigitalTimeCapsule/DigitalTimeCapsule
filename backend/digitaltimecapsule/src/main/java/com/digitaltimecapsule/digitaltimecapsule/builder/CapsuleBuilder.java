package com.digitaltimecapsule.digitaltimecapsule.builder;

import com.digitaltimecapsule.digitaltimecapsule.model.Capsule;
import com.digitaltimecapsule.digitaltimecapsule.model.CapsuleData;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CapsuleBuilder {
    private String name;
    private LocalDateTime expiryDateTime;
    private boolean opened = false;
    private List<CapsuleData> capsuleDataList = new ArrayList<>();

    public CapsuleBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public CapsuleBuilder setExpiryDateTime(LocalDateTime expiryDateTime) {
        this.expiryDateTime = expiryDateTime;
        return this;
    }

    public CapsuleBuilder setOpened(boolean opened) {
        this.opened = opened;
        return this;
    }

    public CapsuleBuilder addCapsuleData(String dataType, String content) {
        CapsuleData data = new CapsuleData();
        data.setDataType(dataType);
        data.setContent(content);
        capsuleDataList.add(data);
        return this;
    }

    public Capsule build() {
        Capsule capsule = new Capsule();
        capsule.setName(name);
        capsule.setExpiryDateTime(expiryDateTime);
        capsule.setOpened(opened);


        for (CapsuleData data : capsuleDataList) {
            data.setCapsule(capsule);
        }
        capsule.getCapsuleDataList().addAll(capsuleDataList);

        return capsule;
    }
}