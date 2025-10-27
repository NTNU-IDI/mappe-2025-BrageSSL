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
    private String email;
    private String phoneNumber;
    private String description;
    private String userPassword;
    private String encodedKey; // Base64 version of SecretKey

    @JsonIgnore
    private SecretKey userKey;

    // Default constructor for Jackson
    @JsonCreator
    public User(
            @JsonProperty("userId") String userId,
            @JsonProperty("userName") String userName,
            @JsonProperty("email") String email,
            @JsonProperty("phoneNumber") String phoneNumber,
            @JsonProperty("description") String description,
            @JsonProperty("userPassword") String userPassword,
            @JsonProperty("encodedKey") String encodedKey) {

        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.description = description;
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
    public User(List<User> users,Scanner scanner) {
        Console console = System.console();
        if (console == null) {
            throw new RuntimeException("---| No console available. Run from a terminal. |---");
        }
        String name;
        while(true){
            System.out.print("| Enter username: ");
            name = scanner.nextLine();
            if(findUser(users, name) != null){
                System.out.println("---| Username already exists. Please choose another. |---");
            } else {
                break;
            }
        }
        this.userName = name;
        this.userId = UUID.randomUUID().toString();

        // Get optional information
        System.out.print("| Enter email (or press Enter to skip): ");
        this.email = scanner.nextLine().trim();
        if (this.email.isEmpty()) {
            this.email = null;
        }

        System.out.print("| Enter phone number (or press Enter to skip): ");
        this.phoneNumber = scanner.nextLine().trim();
        if (this.phoneNumber.isEmpty()) {
            this.phoneNumber = null;
        }

        System.out.print("| Enter description/bio (or press Enter to skip): ");
        this.description = scanner.nextLine().trim();
        if (this.description.isEmpty()) {
            this.description = null;
        }

        String password = passwordEntry(console);
        encryptPassword(password);


    System.out.println("~~~~| Successfully created User: " + userName +" |~~~~");
    }

    private String passwordEntry(Console console) {
        while (true) {
            char[] tempPassword = console.readPassword("| Enter password: ");
            char[] tempPassword2 = console.readPassword("| Repeat password: ");

            try {
                if (Arrays.equals(tempPassword, tempPassword2)) {
                    return new String(tempPassword);
                }
                System.out.println("---| Passwords do not match. Please try again. |---");
            } finally {
                // Always clear passwords from memory
                Arrays.fill(tempPassword, ' ');
                Arrays.fill(tempPassword2, ' ');
            }
        }
    }

    // Authentication logic
    public void passwordAuth(String userName, Scanner scanner) throws Exception {
        Console console = System.console();
        if (console == null) {
            throw new Exception("---| No console available. Run from a terminal. |---");
        }
        char[] passwordChars = console.readPassword("| Enter password: ");
        String inputPassword = new String(passwordChars);
        Arrays.fill(passwordChars, ' '); // Clear password from memory

        String hashedInputPassword = EncryptionUtil.hashPassword(inputPassword, getUserKey());
        if (!userName.equals(getUserName()) || !hashedInputPassword.equals(getUserPassword())) {
            System.out.print("---| Authentication failed: Incorrect username or password |---");
            try {
                Thread.sleep(3000); // wait 3 seconds before throwing
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                
            }
            throw new Exception();
        }
    }

    public static User userAuth(List<User> users, Scanner scanner){
        while (true){
            System.out.print("| Enter username: ");
            String userName = scanner.nextLine();

            User found = findUser(users, userName);

            if (found == null){
                DiaryRead.clearConsole();
                System.out.println("---| User not found, try again. |---");
                continue;
            }
            try {
                // Call the instance method on the found user
                found.passwordAuth(userName, scanner);
                System.out.println("\n\n\n=====Login succsess=====");
                return found; // successful login, return this user
            } catch (Exception e) {
                DiaryRead.clearConsole();
                System.out.println("---| Wrong Password Try again. |---");
            }
        }
    }

    private void encryptPassword(String password){
        try {
            this.userKey = EncryptionUtil.generateSecretKey();
            this.userPassword = EncryptionUtil.hashPassword(password, userKey);
            this.encodedKey = Base64.getEncoder().encodeToString(userKey.getEncoded());
        } catch (Exception e) {
            throw new RuntimeException("---| Failed to create user |---", e);
        }
    }

    public static User findUser(List<User> users, String userName) {
        return users.stream()
            .filter(u -> u.getUserName().equals(userName))
            .findFirst()
            .orElse(null);
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
    public String getEmail() {return email;}
    public String getPhoneNumber() {return phoneNumber;}
    public String getDescription() {return description;}
    public String getUserPassword() {return userPassword;}
    public String getEncodedKey() {return encodedKey;}

    public void setEmail(String email) {this.email = email;}
    public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}
    public void setDescription(String description) {this.description = description;}
}