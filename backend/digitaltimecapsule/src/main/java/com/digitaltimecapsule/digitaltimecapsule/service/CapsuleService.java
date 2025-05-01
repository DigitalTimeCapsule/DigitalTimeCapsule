package com.digitaltimecapsule.digitaltimecapsule.service;

import com.digitaltimecapsule.digitaltimecapsule.builder.CapsuleBuilder;
import com.digitaltimecapsule.digitaltimecapsule.model.Capsule;
import com.digitaltimecapsule.digitaltimecapsule.model.CapsuleData;
import com.digitaltimecapsule.digitaltimecapsule.repository.CapsuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.HashMap;
import java.util.Map;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class CapsuleService {

    @Value("${upload.dir}")
    private String uploadDir;

    @Autowired
    private CapsuleRepository capsuleRepository;

    public List<Capsule> getAllCapsules() {
        return capsuleRepository.findAll();
    }

    public void createCapsule(String title, String message, LocalDateTime openDate,
                              List<MultipartFile> images,
                              List<MultipartFile> videos,
                              List<MultipartFile> files) throws IOException {

        CapsuleBuilder builder = new CapsuleBuilder()
                .setName(title)
                .setExpiryDateTime(openDate)
                .setOpened(false);

        Capsule capsule = builder.build();

        List<CapsuleData> capsuleDataList = new ArrayList<>();

        if (message != null && !message.isBlank()) {
            CapsuleData textData = new CapsuleData();
            textData.setDataType("text");
            textData.setContent(message);
            textData.setCapsule(capsule);
            capsuleDataList.add(textData);
        }

        capsuleDataList.addAll(handleFileGroup(images, "image", capsule));
        capsuleDataList.addAll(handleFileGroup(videos, "video", capsule));
        capsuleDataList.addAll(handleFileGroup(files, "file", capsule));

        capsule.setCapsuleDataList(capsuleDataList);

        capsuleRepository.save(capsule);
    }


    private List<CapsuleData> handleFileGroup(List<MultipartFile> files, String type, Capsule capsule) throws IOException {
        List<CapsuleData> dataList = new ArrayList<>();
        if (files == null) return dataList;

        for (MultipartFile file : files) {
            String filePath = saveFile(file);

            CapsuleData data = new CapsuleData();
            data.setDataType(type);
            data.setContent(filePath);
            data.setCapsule(capsule);
            dataList.add(data);
        }
        return dataList;
    }

    private String saveFile(MultipartFile file) throws IOException {
        // Calculate file hash
        String fileHash = calculateFileHash(file);
        
        // Check if file with same hash exists
        String existingFilePath = findFileByHash(fileHash);
        if (existingFilePath != null) {
            return existingFilePath;
        }

        // Generate secure filename
        String secureFileName = generateSecureFileName(file.getOriginalFilename());
        Path filePath = Paths.get(uploadDir, secureFileName);
        
        // Create directories if they don't exist
        Files.createDirectories(filePath.getParent());
        
        // Save the file
        Files.write(filePath, file.getBytes());
        
        // Store the hash in a separate file for future reference
        storeFileHash(secureFileName, fileHash);
        
        return filePath.toString();
    }

    private String calculateFileHash(MultipartFile file) throws IOException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(file.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error calculating file hash", e);
        }
    }

    private String findFileByHash(String hash) throws IOException {
        Path hashFile = Paths.get(uploadDir, "file_hashes.txt");
        if (!Files.exists(hashFile)) {
            return null;
        }

        List<String> lines = Files.readAllLines(hashFile);
        for (String line : lines) {
            String[] parts = line.split(":");
            if (parts.length == 2 && parts[1].equals(hash)) {
                return Paths.get(uploadDir, parts[0]).toString();
            }
        }
        return null;
    }

    private void storeFileHash(String fileName, String hash) throws IOException {
        Path hashFile = Paths.get(uploadDir, "file_hashes.txt");
        String entry = fileName + ":" + hash + "\n";
        Files.write(hashFile, entry.getBytes(), java.nio.file.StandardOpenOption.CREATE, java.nio.file.StandardOpenOption.APPEND);
    }

    private String generateSecureFileName(String originalFileName) {
        String extension = "";
        int lastDot = originalFileName.lastIndexOf('.');
        if (lastDot > 0) {
            extension = originalFileName.substring(lastDot);
        }
        return UUID.randomUUID().toString() + extension;
    }

    public List<Capsule> getAllUnopenedCapsules() {
        return capsuleRepository.findByOpenedFalse();
    }

    public void markCapsuleAsOpened(Long id) {
        Capsule capsule = capsuleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Capsule not found with id: " + id));
        capsule.setOpened(true);
        capsuleRepository.save(capsule);
    }

    public List<Capsule> getOpenedCapsules() {
        return capsuleRepository.findByOpenedTrue();
    }

    public Map<String, Object> convertToDto(Capsule capsule) {
        Map<String, Object> dto = new HashMap<>();
        dto.put("id", capsule.getId());
        dto.put("title", capsule.getName());
        dto.put("expiryDateTime", capsule.getExpiryDateTime());
        dto.put("opened", capsule.isOpened());

        List<String> imageUrls = new ArrayList<>();
        List<String> videoUrls = new ArrayList<>();
        List<String> fileUrls = new ArrayList<>();
        String message = null;

        for (CapsuleData data : capsule.getCapsuleDataList()) {
            switch (data.getDataType()) {
                case "text" -> message = data.getContent();
                case "image" -> imageUrls.add(convertPathToUrl(data.getContent()));
                case "video" -> videoUrls.add(convertPathToUrl(data.getContent()));
                case "file" -> fileUrls.add(convertPathToUrl(data.getContent()));
            }
        }

        dto.put("message", message);
        dto.put("imageUrls", imageUrls);
        dto.put("videoUrls", videoUrls);
        dto.put("fileUrls", fileUrls);

        return dto;
    }

    private String convertPathToUrl(String path) {
        String fixedPath = path.replace("\\", "/");
        return "http://localhost:8080/" + fixedPath;
    }

}
