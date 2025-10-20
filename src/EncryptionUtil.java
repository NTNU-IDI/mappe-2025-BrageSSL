
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.nio.charset.StandardCharsets;

public class EncryptionUtil {

    private static SecretKey secretKey;
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final int IV_SIZE = 16;

    public static byte[] encrypt(String content, SecretKey key) throws Exception {
        byte[] iv = new byte[IV_SIZE];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
        return cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
    }

    public static String decrypt(byte[] encryptedContent, SecretKey key) throws Exception {
        IvParameterSpec ivSpec = new IvParameterSpec(new byte[IV_SIZE]);  // This is just a placeholder

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
        byte[] decryptedBytes = cipher.doFinal(encryptedContent);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    public static SecretKey generateSecretKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
        keyGen.init(256); // 256-bit AES key
        secretKey = keyGen.generateKey();
        return secretKey;
    }

    public static String hashPassword(String password, SecretKey secretKey) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(secretKey.getEncoded());
        byte[] hashedBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hashedBytes);
    }

    public static SecretKey getSecretKey() {
        return secretKey;
    }
}
