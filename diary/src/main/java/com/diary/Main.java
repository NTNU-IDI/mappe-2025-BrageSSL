package com.diary;

import java.io.File;
import java.util.List;
import java.util.Scanner;

import com.diary.manager.DiaryManager;
import com.diary.manager.UserManager;
import com.diary.manager.EntryManager;
import com.diary.model.DiaryEntry;
import com.diary.model.User;
import com.diary.model.Locations;
import com.diary.model.Moods;
import com.diary.util.Interfaces;
import com.diary.util.DiaryRead;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class Main {

    /**
     * Main method - entry point of the application.    
     * @param args
     */
    public static void main(String[] args) {

        User user = null;
        boolean exit = false;
        String choice = "";

        /**
         * Initialize ObjectMapper for JSON operations.
         */
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.enable(SerializationFeature.INDENT_OUTPUT); 

        // ---------- Load users ----------
        /**
         * Load users from JSON file.
         */
        File userFile = new File("diary/data/user.json");
        List<User> users = DiaryManager.loadUser(userFile, mapper);

        // ---------- Load diary entries ----------
        /**
         * Load diary entries from JSON file.
         */
        File diaryFile = new File("diary/data/diary.json");
        List<DiaryEntry> entries = DiaryManager.loadEntries(diaryFile, mapper);
        
        // ---------- Load locations ----------
        /** 
         * Load locations from JSON file.
         */
        File locationFile = new File("diary/data/location.json");
        List<Locations> locations = DiaryManager.loadLocations(locationFile, mapper);
        
        // ---------- Load moods ----------
        /** 
         * Load moods from JSON file.
         */
        File moodFile = new File("diary/data/mood.json");
        List<Moods> moods = DiaryManager.loadMood(moodFile, mapper);

        /** 
         * Initialize UserManager and EntryManager with loaded data.
        */
        UserManager userManager = new UserManager(users);
        EntryManager entryManager = new EntryManager(entries, locations, moods);

        /** 
         * Main application loop for user interaction.
        */
        Interfaces.clearConsole();
        Scanner scanner = new Scanner(System.in);
        if (users.isEmpty()) {
            Interfaces.errorMessageNoUserFound();
            user = userManager.createUser(scanner);
            userManager.newUser(scanner, mapper, userFile);
        }

        /** 
         * Application loop for user authentication and menu navigation.
        */
        Interfaces.clearConsole();
        while (exit!=true) {
            if (user == null) {
                Interfaces.messageUserCreateOrLogin();
                choice = scanner.nextLine().trim();
                switch (choice) {
                    case "1":
                        Interfaces.clearConsole();
                        user = userManager.createUser(scanner);
                        userManager.newUser(scanner, mapper, userFile);
                        break;
                    case "2":
                        try {
                            Interfaces.clearConsole();
                            user = userManager.userAuth(scanner);
                        } catch (Exception e) {
                            Interfaces.errorMessageAuthFailed();
                            continue;
                        }
                    default:
                        Interfaces.errorMessageNumber();
                        continue;
                }
            }
            Interfaces.clearPlusUser(user);
            while (user != null) {
                Interfaces.menuOptions();
                choice = scanner.nextLine().trim();
                switch (choice) {
                    case "1":
                        Interfaces.clearPlusUser(user);
                        entryManager.createEntry(user, scanner, mapper, diaryFile, locationFile, moodFile);
                        break;
                    case "2":
                        Interfaces.clearPlusUser(user);
                        entryManager.sortOptions(user, scanner, diaryFile);
                        break;
                    case "3":
                        Interfaces.clearPlusUser(user);
                        entryManager.sortOptions(null, scanner, diaryFile);
                        break;
                    case "4":
                        Interfaces.clearPlusUser(user);
                        Interfaces.messagePromptAuthorName();
                        String otherUserName = scanner.nextLine().trim();
                        DiaryRead.otherIndex(user, otherUserName, diaryFile);
                        break;
                    case "5":
                        Interfaces.clearConsole();
                        Interfaces.messageLogout();
                        user = null;
                        break;
                    case "6":
                        Interfaces.clearPlusUser(user);
                        entryManager.updateEntry(user, mapper, diaryFile, scanner);
                        break;
                        
                    case "7":
                        Interfaces.clearPlusUser(user);
                        userManager.profileSettings(user, scanner, mapper, userFile);
                        break;
                    case "8":
                        Interfaces.clearConsole();
                        Interfaces.messageExit();
                        exit = true;
                        user = null;
                        break;
                    default:
                        Interfaces.clearConsole();
                        Interfaces.errorMessageNumber();
                        break;
                }        
            }
        }
    }
}