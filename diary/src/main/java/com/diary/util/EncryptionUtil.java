package com.diary.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;


public class EncryptionUtil {

    // Creates secret key variable
    private static SecretKey secretKey;
    private static final String ALGORITHM = "AES";

    // Encrypts the given content using the provided secret key
    public static byte[] encrypt(String content, SecretKey key) throws Exception {
        // Create cipher instance and initialize it for encryption
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
    }

    // Decrypts the given encrypted content using the provided secret key
    public static String decrypt(byte[] encryptedContent, SecretKey key) throws Exception {
        // Create cipher instance and initialize it for decryption
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedBytes = cipher.doFinal(encryptedContent);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    // Generates a new secret key for encryption/decryption
    public static SecretKey generateSecretKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
        keyGen.init(256); // 256-bit AES key
        secretKey = keyGen.generateKey();
        return secretKey;
    }

    // Hashes the given password using SHA-256 and the provided secret key
    public static String hashPassword(String password, SecretKey secretKey) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(secretKey.getEncoded());
        byte[] hashedBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hashedBytes);
    }

    // Getter for the secret key
    public static SecretKey getSecretKey() {return secretKey;}
}
