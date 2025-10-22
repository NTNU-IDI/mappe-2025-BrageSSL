
import java.util.Arrays;
import java.util.Base64;
import java.util.Scanner;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class User {

    private String userId;
    private String userName;
    private String userPassword;
    private String encodedKey; // Base64 version of SecretKey

    @JsonIgnore
    private SecretKey userKey;

    // Default constructor for Jackson
    public User() {
    }

    // Constructor for interactive user creation
    public User(boolean interactive) {
        if (!interactive) {
            return; // only do this if true
        }
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter username: ");
        this.userName = scanner.nextLine();
        this.userId = java.util.UUID.randomUUID().toString();

        char[] tempPassword;
        char[] tempPassword2;

        while (true) {
            System.out.print("Enter password: ");
            tempPassword = scanner.nextLine().toCharArray();
            System.out.print("Repeat password: ");
            tempPassword2 = scanner.nextLine().toCharArray();
            if (Arrays.equals(tempPassword, tempPassword2)) {
                Arrays.fill(tempPassword2, ' ');
                break;
            }
            System.out.println("Passwords do not match. Please try again.");
            Arrays.fill(tempPassword, ' ');
            Arrays.fill(tempPassword2, ' ');
        }

        try {
            this.userKey = EncryptionUtil.generateSecretKey();
            this.userPassword = EncryptionUtil.hashPassword(new String(tempPassword), userKey);
            this.encodedKey = Base64.getEncoder().encodeToString(userKey.getEncoded());
            Arrays.fill(tempPassword, ' ');
        } catch (Exception e) {
            throw new RuntimeException("Failed to create user", e);
        }

        System.out.println("Successfully created User: " + userName);
    }

    // Getters for Jackson
    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getEncodedKey() {
        return encodedKey;
    }

    @JsonIgnore
    public SecretKey getUserKey() {
        if (userKey == null && encodedKey != null) {
            byte[] decoded = Base64.getDecoder().decode(encodedKey);
            userKey = new SecretKeySpec(decoded, 0, decoded.length, "AES");
        }
        return userKey;
    }

    // Authentication logic
    public void UserAuth() throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter username: ");
        String inputUsername = scanner.nextLine();

        System.out.print("Enter password: ");
        String inputPassword = scanner.nextLine();

        String hashedInputPassword = EncryptionUtil.hashPassword(inputPassword, getUserKey());
        if (!inputUsername.equals(getUserName()) || !hashedInputPassword.equals(getUserPassword())) {
            throw new Exception("Authentication failed: Incorrect username or password");
        }
    }
}
