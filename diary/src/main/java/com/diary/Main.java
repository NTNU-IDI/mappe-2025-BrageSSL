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
        Scanner scanner = new Scanner(System.in);
        if (users.isEmpty()) {
            Interfaces.errorMessageNoUserFound();
            user = userManager.createUser(scanner);
            userManager.newUser(scanner, mapper, userFile);
            DiaryRead.clearConsole();
        }

        /** 
         * Application loop for user authentication and menu navigation.
        */
        while (exit!=true) {
            if (user == null) {
                DiaryRead.clearConsole();
                Interfaces.messageUserCreateOrLogin();
                choice = scanner.nextLine().trim();
                switch (choice) {
                    case "1":
                        user = userManager.createUser(scanner);
                        userManager.newUser(scanner, mapper, userFile);
                        break;
                    case "2":
                        try {
                            user = userManager.userAuth(scanner);
                            DiaryRead.clearConsole();
                            Interfaces.currentUser(user);
                        } catch (Exception e) {
                            Interfaces.errorMessageAuthFailed();
                            System.exit(0);
                        }
                    default:
                        Interfaces.errorMessageNumber();
                        continue;
                }
            }
            DiaryRead.clearConsole();
            Interfaces.currentUser(user);
            while (user != null) {
                Interfaces.menuOptions();
                choice = scanner.nextLine().trim();
                switch (choice) {
                    case "1":
                        DiaryRead.clearConsole();
                        Interfaces.currentUser(user);
                        entryManager.createEntry(user, scanner, mapper, diaryFile, locationFile, moodFile);
                        break;
                    case "2":
                        DiaryRead.clearConsole();
                        Interfaces.currentUser(user);
                        entryManager.sortOptions(user, scanner, diaryFile);
                        break;
                    case "3":
                        DiaryRead.clearConsole();
                        Interfaces.currentUser(user);
                        entryManager.sortOptions(null, scanner, diaryFile);
                        break;
                    case "4":
                        DiaryRead.clearConsole();
                        Interfaces.currentUser(user);
                        Interfaces.messagePromptAuthorName();
                        String otherUserName = scanner.nextLine().trim();
                        DiaryRead.otherIndex(otherUserName, diaryFile);
                        break;
                    case "5":
                        DiaryRead.clearConsole();
                        Interfaces.messageLogout();
                        user = null;
                        break;
                    case "6":
                        DiaryRead.clearConsole();
                        Interfaces.currentUser(user);
                        userManager.profileSettings(user, scanner, mapper, userFile);
                        break;
                    case "7":
                        DiaryRead.clearConsole();
                        Interfaces.messageExit();
                        exit = true;
                        user = null;
                        break;
                    default:
                        Interfaces.errorMessageNumber();
                        break;
                }        
            }
        }
    }
}