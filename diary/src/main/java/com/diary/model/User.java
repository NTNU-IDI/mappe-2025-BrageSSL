package com.diary.model;

import java.io.Console;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.Scanner;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.diary.util.DiaryRead;
import com.diary.util.EncryptionUtil;
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
    @JsonCreator
    public User(
            @JsonProperty("userId") String userId,
            @JsonProperty("userName") String userName,
            @JsonProperty("userPassword") String userPassword,
            @JsonProperty("encodedKey") String encodedKey) {

        this.userId = userId;
        this.userName = userName;
        this.userPassword = userPassword;
        this.encodedKey = encodedKey;

        if (encodedKey != null) {
            byte[] decoded = Base64.getDecoder().decode(encodedKey);
            this.userKey = new SecretKeySpec(decoded, 0, decoded.length, "AES");
        } else {
            this.userKey = null;
        }
    }

    // Constructor for interactive user creation
    public User(Scanner scanner) {
        Console console = System.console();
        if (console == null) {
            throw new RuntimeException("No console available. Run from a terminal.");
        }

        System.out.print("Enter username: ");
        this.userName = scanner.nextLine();
        this.userId = UUID.randomUUID().toString();

        char[] tempPassword;
        char[] tempPassword2;

        while (true) {
            tempPassword = console.readPassword("Enter password: ");
            tempPassword2 = console.readPassword("Repeat password: ");

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

    // Authentication logic
    public void passwordAuth(String userName, Scanner scanner) throws Exception {
        Console console = System.console();
        if (console == null) {
            throw new Exception("No console available. Run from a terminal.");
        }
        char[] passwordChars = console.readPassword("Enter password: ");
        String inputPassword = new String(passwordChars);
        Arrays.fill(passwordChars, ' '); // Clear password from memory

        String hashedInputPassword = EncryptionUtil.hashPassword(inputPassword, getUserKey());
        if (!userName.equals(getUserName()) || !hashedInputPassword.equals(getUserPassword())) {
            
            try {
                Thread.sleep(3000); // wait 3 seconds before throwing
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.print("Authentication failed: Incorrect username or password");
            }
            throw new Exception();
        }
    }
    public static User userAuth(List<User> users, Scanner scanner){
        while (true){
            System.out.print("Enter username: ");
            String userName = scanner.nextLine();

            User found = null;
            for (User u : users) {
                if (u.getUserName().equals(userName)){
                    found = u;
                    break;
                }
            }
            if (found == null){
                DiaryRead.clearConsole();
                System.out.println("User not found, try again.");
                continue;
            }
            try {
                // Call the instance method on the found user
                found.passwordAuth(userName, scanner);
                System.out.println("\n\n\n=====Login succsess=====");
                return found; // successful login, return this user
            } catch (Exception e) {
                DiaryRead.clearConsole();
                System.out.println(e.getMessage() + " Try again.");
            }
        }
    }

    @JsonIgnore
    public SecretKey getUserKey() {
        if (userKey == null && encodedKey != null) {
            byte[] decoded = Base64.getDecoder().decode(encodedKey);
            userKey = new SecretKeySpec(decoded, 0, decoded.length, "AES");
        }
        return userKey;
    }

    // Getters for Jackson
    public String getUserId() {return userId;}
    public String getUserName() {return userName;}
    public String getUserPassword() {return userPassword;}
    public String getEncodedKey() {return encodedKey;}
}
