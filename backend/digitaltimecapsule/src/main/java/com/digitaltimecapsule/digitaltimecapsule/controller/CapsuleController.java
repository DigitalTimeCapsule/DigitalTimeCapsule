package com.digitaltimecapsule.digitaltimecapsule.controller;

import com.digitaltimecapsule.digitaltimecapsule.service.CapsuleService;
import com.digitaltimecapsule.digitaltimecapsule.model.Capsule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/capsules")
@CrossOrigin(origins = "*")
public class CapsuleController {

    @Autowired
    private CapsuleService capsuleService;

    @GetMapping
    public List<Capsule> getAllCapsules() {
        return capsuleService.getAllCapsules();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createCapsule(
            @RequestParam("title") String title,
            @RequestParam(value = "message", required = false) String message,
            @RequestParam("openDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime openDate,
            @RequestParam(value = "images", required = false) List<MultipartFile> images,
            @RequestParam(value = "videos", required = false) List<MultipartFile> videos,
            @RequestParam(value = "files", required = false) List<MultipartFile> files
    ) {
        try {
            capsuleService.createCapsule(title, message, openDate, images, videos, files);
            return ResponseEntity.ok("Capsule created successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create capsule.");
        }
    }

    @GetMapping("/unopened")
    public List<Capsule> getAllUnopenedCapsules() {
        return capsuleService.getAllUnopenedCapsules();
    }

    @PutMapping("/{id}/mark-opened")
    public ResponseEntity<String> markCapsuleAsOpened(@PathVariable Long id) {
        try {
            capsuleService.markCapsuleAsOpened(id);
            return ResponseEntity.ok("Capsule marked as opened.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to mark capsule as opened.");
        }
    }

    @GetMapping("/opened")
    public ResponseEntity<List<Map<String, Object>>> getOpenedCapsules() {
        List<Capsule> openedCapsules = capsuleService.getOpenedCapsules();
        List<Map<String, Object>> capsuleDTOs = openedCapsules.stream()
                .map(capsuleService::convertToDto)
                .toList();
        return ResponseEntity.ok(capsuleDTOs);
    }

}
