package com.digitaltimecapsule.digitaltimecapsule.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EncryptionServiceTest {

    @Autowired
    private EncryptionService encryptionService;

    private Path testDir;
    private Path originalFile;
    private Path encryptedFile;
    private Path decryptedFile;

    @BeforeEach
    void setUp() throws IOException {
        // Create a test directory
        testDir = Files.createTempDirectory("encryption-test");
        
        // Create test files
        originalFile = testDir.resolve("original.txt");
        encryptedFile = testDir.resolve("encrypted.dat");
        decryptedFile = testDir.resolve("decrypted.txt");
        
        // Write some test content
        String testContent = "This is a test file for encryption verification!";
        Files.write(originalFile, testContent.getBytes());
    }

    @Test
    void testFileEncryptionAndDecryption() throws Exception {
        // Encrypt the file
        encryptionService.encryptFile(originalFile, encryptedFile);
        
        // Verify the encrypted file is different from original
        byte[] originalBytes = Files.readAllBytes(originalFile);
        byte[] encryptedBytes = Files.readAllBytes(encryptedFile);
        assertFalse(Arrays.equals(originalBytes, encryptedBytes), 
            "Encrypted file should be different from original");
        
        // Decrypt the file
        encryptionService.decryptFile(encryptedFile, decryptedFile);
        
        // Verify the decrypted content matches the original
        byte[] decryptedBytes = Files.readAllBytes(decryptedFile);
        assertArrayEquals(originalBytes, decryptedBytes, 
            "Decrypted content should match original content");
    }

    @Test
    void testTextEncryptionAndDecryption() {
        String originalText = "This is a test message!";
        
        // Encrypt the text
        String encryptedText = encryptionService.encrypt(originalText);
        
        // Verify encrypted text is different from original
        assertNotEquals(originalText, encryptedText, 
            "Encrypted text should be different from original");
        
        // Decrypt the text
        String decryptedText = encryptionService.decrypt(encryptedText);
        
        // Verify decrypted text matches original
        assertEquals(originalText, decryptedText, 
            "Decrypted text should match original text");
    }
} 