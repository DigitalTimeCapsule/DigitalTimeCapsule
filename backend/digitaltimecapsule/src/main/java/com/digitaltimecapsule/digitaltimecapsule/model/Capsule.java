package com.digitaltimecapsule.digitaltimecapsule.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Capsule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private LocalDateTime expiryDateTime;

    private boolean opened;

    @OneToMany(mappedBy = "capsule", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<CapsuleData> capsuleDataList = new ArrayList<>();

    // Getters, setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public LocalDateTime getExpiryDateTime() {
        return expiryDateTime;
    }

    public void setExpiryDateTime(LocalDateTime expiryDateTime) {
        this.expiryDateTime = expiryDateTime;
    }

    public List<CapsuleData> getCapsuleDataList() {
        return capsuleDataList;
    }

    public void setCapsuleDataList(List<CapsuleData> capsuleDataList) {
        this.capsuleDataList = capsuleDataList;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}