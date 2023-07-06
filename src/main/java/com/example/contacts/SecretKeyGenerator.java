package com.example.contacts;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class SecretKeyGenerator {
    private static SecretKeyGenerator instance;
    private String secretKey;

    private SecretKeyGenerator() {
        this.secretKey = generateSecretKey();
    }

    public static SecretKeyGenerator getInstance(){
        if(instance == null){
            instance = new SecretKeyGenerator();
        }
        return instance;
    }

    public String getSecretKey() {
        return secretKey;
    }

    private String generateSecretKey() {
        // Generate a secure random byte array
        byte[] randomBytes = new byte[32];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(randomBytes);

        // Convert the byte array to a base64-encoded string
        String base64Encoded = Base64.getEncoder().encodeToString(randomBytes);

        // Generate a SHA-256 hash of the base64-encoded string
        String secretKey = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(base64Encoded.getBytes(StandardCharsets.UTF_8));
            secretKey = Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return secretKey;
    }
}
