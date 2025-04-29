package com.digitaltimecapsule.digitaltimecapsule.controller;

import com.digitaltimecapsule.digitaltimecapsule.service.CapsuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
@RestController
@RequestMapping("/api/capsules")
@CrossOrigin(origins = "*")
public class CapsuleController {

    @Autowired
    private CapsuleService capsuleService;

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
}
