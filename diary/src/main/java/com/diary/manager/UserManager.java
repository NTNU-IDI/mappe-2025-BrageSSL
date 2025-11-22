package com.diary.manager;

import com.diary.model.User;
import com.diary.util.DiaryRead;
import com.diary.util.EncryptionUtil;
import com.diary.util.Interfaces;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import java.io.Console;

import javax.crypto.SecretKey;

public class UserManager {
    private ArrayList<User> users;

    public void createUser() {
        Scanner scanner = new Scanner(System.in);

        String userId = UUID.randomUUID().toString();

        String[] parts = createPassword();
        String userPassword = parts[0];
        String encodedKey = parts[1];

        String userName = createUserName(scanner);

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
    }

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

    // Authentication logic
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
    
    public User findUser(String userName) {
        for (User user : users) {
            if (user.getUserName().equals(userName)) {
                return user;
            }
        }
        return null;
    }

    public ArrayList<User> getUsers() {
        return users;
    }
}
