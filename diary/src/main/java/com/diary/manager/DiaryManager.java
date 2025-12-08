package com.diary.manager;

import com.diary.model.DiaryEntry;
import com.diary.model.Locations;
import com.diary.model.Moods;
import com.diary.model.User;
import com.diary.util.Interfaces;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.ArrayList;

/**
 * DiaryManager class for managing diary-related operations.
 */
public class DiaryManager {
    /**
     * Load users from file.
     * 
     * @param fileName File to load users from.
     * @param mapper   ObjectMapper for JSON operations.
     * @return List of loaded User objects.
     */
    public static ArrayList<User> loadUser(File fileName, ObjectMapper mapper) {
        // ---------- Load users ----------
        File userFile = fileName;
        ArrayList<User> users = new ArrayList<>();
        try {
            if (userFile.exists() && userFile.length() > 0) {
                User[] loadedUsers = mapper.readValue(userFile, User[].class);
                for (User u : loadedUsers) {
                    users.add(u);
                }
            }
        } catch (Exception e) {
            Interfaces.errorMessageUnableToLoad();
        }
        return users;
    }

    /**
     * Load diary entries from file.
     * 
     * @param fileName File to load diary entries from.
     * @param mapper   ObjectMapper for JSON operations.
     * @return List of loaded DiaryEntry objects.
     */
    public static ArrayList<DiaryEntry> loadEntries(File fileName, ObjectMapper mapper) {
        // ---------- Load diary entries ----------
        File diaryFile = fileName;
        ArrayList<DiaryEntry> entries = new ArrayList<>();
        try {
            if (diaryFile.exists() && diaryFile.length() > 0) {
                DiaryEntry[] loadedDiary = mapper.readValue(diaryFile, DiaryEntry[].class);
                for (DiaryEntry e : loadedDiary)
                    entries.add(e);
            }
        } catch (Exception e) {
            Interfaces.errorMessageUnableToLoad();
        }
        return entries;
    }

    /**
     * Load locations from file.
     * 
     * @param fileName File to load locations from.
     * @param mapper   ObjectMapper forF JSON operations.
     * @return List of loaded Locations objects.
     */
    public static ArrayList<Locations> loadLocations(File fileName, ObjectMapper mapper) {
        // ---------- Load locations ----------
        File locationsFile = fileName;
        ArrayList<Locations> locations = new ArrayList<>();
        try {
            if (locationsFile.exists() && locationsFile.length() > 0) {
                Locations[] loadedLocations = mapper.readValue(locationsFile, Locations[].class);
                for (Locations l : loadedLocations)
                    locations.add(l);
            }
        } catch (Exception e) {
            Interfaces.errorMessageUnableToLoad();
        }
        return locations;
    }

    /**
     * Load moods from file.
     * 
     * @param fileName File to load moods from.
     * @param mapper   ObjectMapper for JSON operations.
     * @return List of loaded Moods objects.
     */
    public static ArrayList<Moods> loadMood(File fileName, ObjectMapper mapper) {
        // ---------- Load diary moods ----------
        File moodFile = fileName;
        ArrayList<Moods> mood = new ArrayList<>();
        try {
            if (moodFile.exists() && moodFile.length() > 0) {
                Moods[] loadedmood = mapper.readValue(moodFile, Moods[].class);
                for (Moods m : loadedmood)
                    mood.add(m);
            }
        } catch (Exception e) {
            Interfaces.errorMessageUnableToLoad();
        }
        return mood;
    }

}