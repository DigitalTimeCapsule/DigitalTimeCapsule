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

@Service
public class CapsuleService {

    @Value("${upload.dir}")
    private String uploadDir;

    @Autowired
    private CapsuleRepository capsuleRepository;

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
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, file.getBytes());
        return filePath.toString();
    }
}
