package com.diary.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.diary.model.DiaryEntry;
import com.diary.model.User;

import com.fasterxml.jackson.databind.ObjectMapper;

public class DiaryManager {

    public static List<User> loadUser (File fileName, ObjectMapper mapper) {
        // ---------- Load users ----------
        File userFile = fileName;
        List<User> users = new ArrayList<>();
        try{
            if (userFile.exists() && userFile.length() > 0) {
                User[] loadedUsers = mapper.readValue(userFile, User[].class);
                for (User u : loadedUsers) users.add(u);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }
    public static List<DiaryEntry> loadEntries (File fileName, ObjectMapper mapper) {
        // ---------- Load diary entries ----------
        File diaryFile = fileName;
        List<DiaryEntry> entries = new ArrayList<>();
        try{
            if (diaryFile.exists() && diaryFile.length() > 0) {
                DiaryEntry[] loadedDiary = mapper.readValue(diaryFile, DiaryEntry[].class);
                for (DiaryEntry e : loadedDiary) entries.add(e);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entries;
    }
    public static List<DiaryManager> loadLocations (File fileName, ObjectMapper mapper) {
        // ---------- Load locations ----------
        File locationsFile = fileName;
        List<DiaryManager> locations = new ArrayList<>();
        try {
            if (locationsFile.exists() && locationsFile.length() > 0) {
                DiaryManager[] loadedLocations = mapper.readValue(locationsFile, DiaryManager[].class);
                for (DiaryManager l : loadedLocations) locations.add(l);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return locations;
    }
    public static List<DiaryManager> loadMood (File fileName, ObjectMapper mapper) {
        // ---------- Load diary moods ----------
        File moodFile = fileName;
        List<DiaryManager> mood = new ArrayList<>();
        try{
            if (moodFile.exists() && moodFile.length() > 0) {
                DiaryManager[] loadedmood = mapper.readValue(moodFile, DiaryManager[].class);
                for (DiaryManager m : loadedmood) mood.add(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mood;
    }
    public static List<DiaryManager> chooseMood(List<DiaryManager> moods, Scanner scanner){
        List<DiaryManager> userMood = new ArrayList<>();
        if () {
                DiaryManager[] loadedmood = mapper.readValue(moodFile, DiaryManager[].class);
                for (DiaryManager m : loadedmood) mood.add(m);
            }
        System.out.println("======= Moods =======");
        for (int i = 0; i < moods.size(); i++) {
            System.out.println((i + 1) + ". " + moods.get(i));
        }
        return mood;
    }
}