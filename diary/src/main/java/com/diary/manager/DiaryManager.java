package com.diary.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.diary.model.DiaryEntry;
import com.diary.model.User;

import com.fasterxml.jackson.databind.ObjectMapper;

public class DiaryManager {
    private String mood;
    private String creator;
    private String location;

    public String getMood() {return mood;}
    public String getCreator() {return creator;}
    public String getLocation() {return location;}
    public void setCreator(String creator) {this.creator = creator;}
    public void setMood(String mood) {this.mood = mood;}
    public void setLocation(String location) {this.location = location;}

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
    public static List<DiaryManager> chooseMood( Scanner scanner, String userName, File moodFile, ObjectMapper mapper) {
        List<DiaryManager> moods = loadMood(moodFile, mapper);
        if (moods == null) moods = new ArrayList<>();
        List<DiaryManager> userMood = new ArrayList<>();

        // Show moods created by the current user
        System.out.println("======= Your Moods =======");
        List<DiaryManager> userCreatedMoods = new ArrayList<>();
        for (DiaryManager m : moods) {
        if (m != null && userName.equals(m.getCreator())) {
            userCreatedMoods.add(m);
        }
}

        for (int i = 0; i < userCreatedMoods.size(); i++) {
            System.out.println((i + 1) + ". " + userCreatedMoods.get(i).getMood());
        }
        
        int choice;
        while (true) {    
            System.out.println((userCreatedMoods.size() + 1) + ". Create a new mood");
            System.out.print("Choose a mood by number: ");
            String input = scanner.nextLine().trim();

            try {
                choice = Integer.parseInt(input); // safely parse
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number a valid number");
            }
        }

        if (choice > 0 && choice <= userCreatedMoods.size()) {
            userMood.add(userCreatedMoods.get(choice - 1));
        } else if (choice == userCreatedMoods.size() + 1) {
            // Create new mood
            System.out.print("Enter your new mood: ");
            String newMoodName = scanner.nextLine();

            DiaryManager newMood = new DiaryManager();
            newMood.setMood(newMoodName);
            newMood.setCreator(userName);

            userMood.add(newMood);
            moods.add(newMood);

            try {
                mapper.writeValue(moodFile, moods);
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("New mood saved!");
        } else {
            System.out.println("Invalid choice.");
        }
        
        return userMood;
    }
    public static List<DiaryManager> chooseLocation( Scanner scanner, String userName, File locationFile, ObjectMapper mapper) {
        List<DiaryManager> locations = loadLocations(locationFile, mapper);
        if (locations == null) locations = new ArrayList<>();
        List<DiaryManager> userLocation = new ArrayList<>();

        // Show locations created by the current user
        System.out.println("======= Your Locations =======");
        List<DiaryManager> userCreatedLocations = new ArrayList<>();
        for (DiaryManager l : locations) {
            if (l != null && userName.equals(l.getCreator())) {
                userCreatedLocations.add(l);
            }
        }

        for (int i = 0; i < userCreatedLocations.size(); i++) {
            System.out.println((i + 1) + ". " + userCreatedLocations.get(i).getLocation());
        }

        int choice;
        while (true) {
            System.out.println((userCreatedLocations.size() + 1) + ". Create a new Location");
            System.out.print("Choose a Location by number: ");

            String input = scanner.nextLine().trim();
            

            try {
                choice = Integer.parseInt(input); // safely parse
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number a valid number");
            }
        }

        if (choice > 0 && choice <= userCreatedLocations.size()) {
            userLocation.add(userCreatedLocations.get(choice - 1));
        } else if (choice == userCreatedLocations.size() + 1) {
            // Create new location
            System.out.print("Enter your new Location: ");
            String newLocationName = scanner.nextLine();

            DiaryManager newLocation = new DiaryManager();
            newLocation.setLocation(newLocationName);
            newLocation.setCreator(userName);

            userLocation.add(newLocation);
            locations.add(newLocation);

            try {
                mapper.writeValue(locationFile, locations);
                System.out.println("New Location saved!");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Failed to save the new location.");
            }
        } else {
            System.out.println("Invalid choice.");
        }

    return userLocation;
    }
}
