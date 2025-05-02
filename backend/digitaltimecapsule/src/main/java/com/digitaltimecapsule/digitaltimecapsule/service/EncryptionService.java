package com.digitaltimecapsule.digitaltimecapsule.service;

import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class EncryptionService {
    private static final String SECRET = "MySuperSecretKeyThatIsAtLeast32CharactersLong!";
    private static final String SALT = "1234567890123456"; // 16 characters
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final int IV_SIZE = 16;

    private final SecretKey secretKey;
    private final SecureRandom secureRandom;

    public EncryptionService() throws Exception {
        // Derive a consistent key from the SECRET
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] keyBytes = digest.digest(SECRET.getBytes());
        this.secretKey = new SecretKeySpec(keyBytes, "AES");
        this.secureRandom = new SecureRandom();
    }

    public String encrypt(String content) {
        TextEncryptor encryptor = Encryptors.text(SECRET, SALT);
        return encryptor.encrypt(content);
    }

    public String decrypt(String encryptedContent) {
        TextEncryptor encryptor = Encryptors.text(SECRET, SALT);
        return encryptor.decrypt(encryptedContent);
    }

    public void encryptFile(Path inputFile, Path outputFile) throws Exception {
        try (FileInputStream fis = new FileInputStream(inputFile.toFile());
             FileOutputStream fos = new FileOutputStream(outputFile.toFile())) {
            
            // Generate IV
            byte[] iv = new byte[IV_SIZE];
            secureRandom.nextBytes(iv);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            
            // Write IV to output file
            fos.write(iv);
            
            // Initialize cipher
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
            
            // Encrypt file
            byte[] inputBytes = Files.readAllBytes(inputFile);
            byte[] encryptedBytes = cipher.doFinal(inputBytes);
            fos.write(encryptedBytes);
        }
    }

    public void decryptFile(Path inputFile, Path outputFile) throws Exception {
        try (FileInputStream fis = new FileInputStream(inputFile.toFile());
             FileOutputStream fos = new FileOutputStream(outputFile.toFile())) {
            
            // Read IV from input file
            byte[] iv = new byte[IV_SIZE];
            fis.read(iv);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            
            // Initialize cipher
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
            
            // Read and decrypt the rest of the file
            byte[] encryptedBytes = fis.readAllBytes();
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            fos.write(decryptedBytes);
        }
    }
} 