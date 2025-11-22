package com.diary.util;

import java.util.List;

import com.diary.model.DiaryEntry;
import com.diary.model.User;

public class Interfaces {
    public static void showEntry(String entryId, List<DiaryEntry> entries, User user) {

        DiaryEntry entry = entries.stream()
                .filter(e -> e.getId().equals(entryId))
                .findFirst()
                .orElse(null);

        if (entry == null) {
            System.out.println("---| No entry found with ID: " + entryId + " |---");
            return;
        }
        System.out.println("~~~~~~ Diary Entry Details ~~~~~~~");
        System.out.println("| Title:      " + entry.getTitle());
        System.out.println("| Author:     " + entry.getAuthor());
        System.out.println("| Date:       " + entry.getDate());
        System.out.println("| Mood:       " + entry.getMood());
        System.out.println("| Location:   " + entry.getLocation());
        if (entry.getEncrypted()) {
            try {
                String decrypted = EncryptionUtil.decrypt(entry.getEncryptedContent(), user.getUserKey());
                System.out.println("Content:\n" + decrypted);
            } catch (Exception e) {
                System.out.println("---| Content: [ENCRYPTED - unable to decrypt] |---");
            }

        } else {
            System.out.println("Content:\n" + entry.getPublicContent());
        }
        System.out.println("================================");
    }

    public static void showOtherEntry(String entryId, List<DiaryEntry> entries) {

        DiaryEntry entry = entries.stream()
                .filter(e -> e.getId().equals(entryId))
                .findFirst()
                .orElse(null);

        if (entry == null) {
            System.out.println("---| No entry found with ID: " + entryId + " |---");
            return;
        }
        System.out.println("~~~~~~ Diary Entry Details ~~~~~~");
        System.out.println("| Title:      " + entry.getTitle());
        System.out.println("| Date:       " + entry.getDate());
        System.out.println("| Mood:       " + entry.getMood());
        System.out.println("| Location:   " + entry.getLocation());
        if (entry.getEncrypted()){}
        else {System.out.println("| Content: " + entry.getPublicContent());}
        System.out.println("================================");
    }

    public static void showIndexEntry(String entryId, List<DiaryEntry> entries) {

        DiaryEntry entry = entries.stream()
                .filter(e -> e.getId().equals(entryId))
                .findFirst()
                .orElse(null);

        if (entry == null) {
            System.out.println("---| No entry found with ID: " + entryId + " |---");
            return;
        }
        System.out.println("~~~~~~ Diary Entry Details ~~~~~~");
        System.out.println("| Author: " + entry.getAuthor());
        System.out.println("| Title:  " + entry.getTitle());
        System.out.println("| Date:   " + entry.getDate());
        System.out.println("================================");
    }

    public static void menuOptions() {
        System.out.println("\n\n\n~~~~~~| What do you want to do? |~~~~~~");
        System.out.println("|1| Create new diary entry");
        System.out.println("|2| See Diary Index");
        System.out.println("|3| See Your diaries");
        System.out.println("|4| See other author's diaries");
        System.out.println("|5| Logout");
        System.out.println("|6| Exit");
        System.out.print  ("|~| ");
    }

    public static void loginMenu() {
        System.out.println("\n\n\n~~~~~~| Welcome to the Diary Application |~~~~~~");
        System.out.println("|1| Login");
        System.out.println("|2| Register");
        System.out.println("|3| Exit");
        System.out.print  ("|~| ");
    }

    public static void encryptionMenu() {
        System.out.println("\n\n\n~~~~~~| Encryption Options |~~~~~~");
        System.out.println("|1| Encrypt diary entries");
        System.out.println("|2| Do not encrypt diary entries");
        System.out.print  ("|~| ");
    }

    public static void errorMessageNumber() {System.out.println("---| Invalid input. Please enter a valid Number. |---");}
    public static void errorMessageText() {System.out.println("---| Invalid input. Please enter valid text. |---");}
    public static void errorMessageNoUserFound() {System.out.println("---| No user found with that username. |---");}
    public static void errorMessageUserExists() {System.out.println("---| User already exists. Please choose a different username. |---");}
    public static void errorMessageInvalidLogin() {System.out.println("---| Authentication failed: Incorrect username or password |---");}
    public static void errorMessagePasswordsDoNotMatch() {System.out.println("---| Passwords do not match. Please try again. |---");}
    public static void errorMessageUnableToLoad() {System.out.println("---| Unable to load data. Please try again. |---");}
    public static void errorMessageNoConsoleFound() {System.out.println("---| No console available. Run from a terminal. |---");}
    public static void errorMessageFailedUserCreation() {System.out.println("---| Failed to create user. Please try again. |---");}
    public static void errorMessageEncryptionFailed() {System.out.println("---| Encryption error occurred. Please try again. |---");}

    
    public static void messagePromptUsername() {System.out.print("| Enter username: ");}
    public static void messagePromptPassword() {System.out.print("| Enter password: ");}
    public static void messagePromptPasswordAgain() {System.out.print("\n| Repeat password: ");}
    public static void messagePromptemail() {System.out.print("| Enter email: ");}
    public static void messagePromptPhone() {System.out.print("| Enter phone number: ");}
    public static void messagePromptDescription() {System.out.print("| Enter description: ");}
    public static void messageUserCreated() {System.out.println("\n\n\n===== User created successfully! =====");}

    public static void messagecreatingEntry() {System.out.println("\n\n\n~~~~~~ Creating a new diary entry ~~~~~~");}
    public static void messagePromptTitle() {System.out.print("| Enter title: ");}
    public static void messagePromptMood() {System.out.print("| Enter mood: ");}
    public static void messagePromptLocation() {System.out.print("| Enter location: ");}
    public static void messagePromptContent() {System.out.print("System.out.println(\"~~~~| Enter diary content (type 'END' on a new line to finish) |~~~~\");");}
    public static void messagePromptEncryptionChoice() {System.out.print("| doo you want to encrypt the content? \n|1| No\n|2| Yes\n|~| ");}
    

    public static void messageLogin() {System.out.println("\n\n\n=====Login succsess=====");}
    public static void messageExit() {System.out.println("~~~~~~| Exiting Diary Application. Goodbye! |~~~~~~");}
}