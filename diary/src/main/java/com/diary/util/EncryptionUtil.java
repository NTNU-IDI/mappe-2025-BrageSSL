package com.diary.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;


public class EncryptionUtil {

    /** 
     * Encryption utility attributes.
    */
    private static SecretKey secretKey;
    private static final String ALGORITHM = "AES";

    /** 
     * Encrypts the given content using the provided secret key.
     * @param content Content to encrypt.
     * @param key SecretKey used for encryption.
     * @return Encrypted content as byte array.
    */
    public static byte[] encrypt(String content, SecretKey key) throws Exception {
        // Create cipher instance and initialize it for encryption
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
    }

    /** 
     * Decrypts the given encrypted content using the provided secret key.
     * @param encryptedContent Encrypted content as byte array.
     * @param key SecretKey used for decryption.
     * @return Decrypted content as String.
    */
    public static String decrypt(byte[] encryptedContent, SecretKey key) throws Exception {
        // Create cipher instance and initialize it for decryption
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedBytes = cipher.doFinal(encryptedContent);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    /** 
     * Generates a new AES SecretKey.
     * @return Generated SecretKey.
    */
    public static SecretKey generateSecretKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
        keyGen.init(256); // 256-bit AES key
        secretKey = keyGen.generateKey();
        return secretKey;
    }

    /** 
     * Hashes the given password using SHA-256 and the provided secret key.
     * @param password Password to hash.
     * @param secretKey SecretKey used for hashing.
     * @return Hashed password as a Base64 encoded string.
    */
    public static String hashPassword(String password, SecretKey secretKey) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(secretKey.getEncoded());
        byte[] hashedBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hashedBytes);
    }

    /** 
     * Encodes the given SecretKey to a Base64 string.
     * @param secretKey SecretKey to encode.
     * @return Encoded SecretKey as Base64 string.
    */
    public static String encodeKey(SecretKey secretKey) {
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    /** 
     * Retrieves the current SecretKey.
     * @return Current SecretKey.
    */
    public static SecretKey getSecretKey() {
        return secretKey;
    }
}
