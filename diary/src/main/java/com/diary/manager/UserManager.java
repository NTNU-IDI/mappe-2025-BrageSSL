package com.diary.manager;

import com.diary.model.User;
import com.diary.util.DiaryRead;
import com.diary.util.EncryptionUtil;
import com.diary.util.Interfaces;

import java.util.ArrayList;
import java.util.Arrays;
import java.io.File;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Scanner;
import java.util.UUID;

import java.io.Console;

import javax.crypto.SecretKey;

public class UserManager {
    /** 
     * List of users.
    */
    private ArrayList<User> users = new ArrayList<>();

    /** 
     * Constructors.
    */
    public UserManager() {}
    public UserManager(List<User> initialUsers) {
        if (initialUsers != null) this.users.addAll(initialUsers);
    }

    /** 
     * Create a new user.
     * @param scanner Scanner for user input.
     * @return Created User object.
    */
    public User createUser(Scanner scanner) {
        String userId = UUID.randomUUID().toString();

        String userName = createUserName(scanner);

        String[] parts = createPassword();
        String userPassword = parts[0];
        String encodedKey = parts[1];

        Interfaces.messagePromptemail();
        String email = scanner.nextLine().trim();
        if (email.isEmpty()) email = null;

        Interfaces.messagePromptPhone();
        String phoneNumber = scanner.nextLine().trim();
        if (phoneNumber.isEmpty()) phoneNumber = null;

        Interfaces.messagePromptDescription();
        String description = scanner.nextLine().trim();
        if (description.isEmpty()) description = null;
        
        User newUser = new User(userId, userName, email, phoneNumber, description, userPassword, encodedKey);
        users.add(newUser);
        
        Interfaces.messageUserCreated();

        return newUser;
    }

    /** 
     * Create a new username.
     * @param scanner Scanner for user input.
     * @return Created username as a String.
    */
    public String createUserName(Scanner scanner) {
        String userName;
        while (true) {
            Interfaces.messagePromptUsername();
            userName = scanner.nextLine();
            if (findUser(userName) != null) {
                DiaryRead.clearConsole();
                Interfaces.errorMessageUserExists();    
            } else {
                break;
            }
        }
        return userName;
    }

    /** 
     * Create a new password.
     * @return Array containing hashed password and encoded key.
    */
    public String[] createPassword(){
        Console console = System.console();
        
        String hashedPassword = null;
        String encodedKey = null;

        while (true) {
            Interfaces.messagePromptPassword();
            char[] tempPassword = console.readPassword("");
            Interfaces.messagePromptPasswordAgain();
            char[] tempPassword2 = console.readPassword("");

            try {
                if (Arrays.equals(tempPassword, tempPassword2)) {
                    if (Arrays.equals(tempPassword, tempPassword2)) {
                        String tempStringPassword = new String(tempPassword);
                        try {
                            SecretKey secretKey = EncryptionUtil.generateSecretKey();
                            hashedPassword = EncryptionUtil.hashPassword(tempStringPassword, secretKey);
                            encodedKey = EncryptionUtil.encodeKey(secretKey);
                        } catch (Exception e) {
                            // catch NoSuchAlgorithmException or other crypto exceptions
                            Interfaces.errorMessageEncryptionFailed();
                        }
                        // clear plain password variable
                        tempStringPassword = null;
                        break;
                    }
                }
                Interfaces.errorMessagePasswordsDoNotMatch();
            } finally {
                // Always clear passwords from memory
                Arrays.fill(tempPassword, ' ');
                Arrays.fill(tempPassword2, ' ');
            }
        }
        return new String[]{hashedPassword, encodedKey};
    }

    /** 
     * Authenticate user password.
     * @param user User to authenticate.
     * @param scanner Scanner for user input.
     * @return True if authentication is successful, false otherwise.
    */
    public boolean passwordAuth(User user, Scanner scanner) throws Exception {
        Console console = System.console();
        if (console == null) {
            Interfaces.errorMessageNoConsoleFound();
            return false;
        }
        Interfaces.messagePromptPassword(); 
        char[] passwordChars = console.readPassword("");
        String inputPassword = new String(passwordChars);
        Arrays.fill(passwordChars, ' '); // Clear password from memory

        String hashedInputPassword = EncryptionUtil.hashPassword(inputPassword, user.getUserKey());
        if (!hashedInputPassword.equals(user.getUserPassword())) {
            Interfaces.errorMessageInvalidLogin();
            try {
                Thread.sleep(3000); // wait 3 seconds before throwing
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            Interfaces.errorMessageNoUserFound();
            return false;
        }
        return true; // successful login, return this user
    }

    /** 
     * Authenticate user.
     * @param scanner Scanner for user input.
     * @return Authenticated User object.
    */
    public User userAuth(Scanner scanner)throws Exception{
        while (true){
            Interfaces.messagePromptUsername();
            String userName = scanner.nextLine();

            User found = findUser(userName);

            if (found == null){
                DiaryRead.clearConsole();
                Interfaces.errorMessageNoUserFound();   
                continue;
            }
            // Call the instance method on the found user
            if (passwordAuth(found, scanner)) {
                Interfaces.messageLogin();
                return found; // successful login, return this user
            }
        }
    }
    
    /** 
     * Find a user by username.
     * @param userName Username to search for.
     * @return Found User object or null if not found.
    */
    public User findUser(String userName) {
        for (User user : users) {
            if (user.getUserName().equals(userName)) {
                return user;
            }
        }
        return null;
    }

    /** 
     * Retrieves the list of users.
     * @return List of users as an ArrayList.
    */
    public ArrayList<User> getUsers() {
        return users;
    }

    /** 
     * Create a new user.
     * @param scanner Scanner for user input.
    */
    public void newUser(Scanner scanner, ObjectMapper mapper, File userFile) {
        try {
            mapper.writeValue(userFile, getUsers());
        } catch (Exception e) {
            Interfaces.errorMessageUnableToWrite();
        }
    }

    /** 
     * Update user profile settings.
     * @param user User whose profile is to be updated.
     * @param scanner Scanner for user input.
     * @param mapper ObjectMapper for JSON operations.
     * @param userFile File to write updated user data to.
    */
    public void profileSettings(User user, Scanner scanner, ObjectMapper mapper, File userFile) {
        Interfaces.messageProfileSettingsMenu();
        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1":
                Interfaces.messagePromptemail();
                String newUserEmail = scanner.nextLine().trim();
                user.setEmail(newUserEmail);
                break;
            case "2":
                Interfaces.messagePromptPhone();
                String newUserPhone = scanner.nextLine().trim();
                user.setPhoneNumber(newUserPhone);
                break;
            case "3":
                Interfaces.messagePromptDescription();
                String newUserDescription = scanner.nextLine().trim();
                if (newUserDescription.isEmpty()) newUserDescription = null;
                user.setDescription(newUserDescription);
                break;
            case "4":
                Interfaces.messagePromptDescription();
                String description = scanner.nextLine().trim();
                if (description.isEmpty()) description = null;
                user.setDescription(description);
                break;
            default:
                Interfaces.errorMessageNumber();
                return;
        }
        try {
            mapper.writeValue(userFile, getUsers());
            DiaryRead.clearConsole();
            Interfaces.messageProfileUpdated();
        } catch (Exception e) {
            Interfaces.errorMessageUnableToWrite();
        }
    }
}
