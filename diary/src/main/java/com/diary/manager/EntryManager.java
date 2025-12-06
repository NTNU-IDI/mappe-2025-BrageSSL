package com.diary.manager;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.diary.model.DiaryEntry;
import com.diary.model.Locations;
import com.diary.model.Moods;
import com.diary.util.EncryptionUtil;
import com.diary.util.Interfaces;
import com.diary.model.User;
import com.diary.util.DiaryRead;

public class EntryManager {
    /** 
     * Lists of entries, locations, and moods.
    */
    private ArrayList<DiaryEntry> entries = new ArrayList<>();
    private ArrayList<Locations> locations = new ArrayList<>();
    private ArrayList<Moods> moods = new ArrayList<>();

    /** 
     * Constructors.
    */
    public EntryManager(List<DiaryEntry> loadedEntries, List<Locations> loadedLocations, List<Moods> loadedMoods) {
        if (loadedEntries != null) this.entries.addAll(loadedEntries);
        if (loadedLocations != null) this.locations.addAll(loadedLocations);
        if (loadedMoods != null) this.moods.addAll(loadedMoods);
    }
    /** 
     * Create a new diary entry.
     * @param user User creating the entry.
     * @param scanner Scanner for user input.
     * @param mapper ObjectMapper for JSON operations.
     * @param entryFile File to write updated entries to.
     * @param locationFile File to write updated locations to.
     * @param moodFile File to write updated moods to.
    */
    public void createEntry(User user, Scanner scanner, ObjectMapper mapper, File entryFile, File locationFile, File moodFile) {
        String id = UUID.randomUUID().toString();
        String author = user.getUserName();
        LocalDate now = LocalDate.now();    

        Interfaces.messagePromptTitle();
        String title = scanner.nextLine().trim();

        Interfaces.messagePromptContent();
        StringBuilder contentBuilder = new StringBuilder();
        String line;
        while (true) {
            line = scanner.nextLine();
            if (line.equalsIgnoreCase("END")) break;
            contentBuilder.append(line).append(System.lineSeparator());
        }
        String content = contentBuilder.toString().trim();

        // choose encryption (1=encrypt, 2=plain)
        boolean encrypt = true;
        while (true) {
            Interfaces.encryptionMenu();
            String input = scanner.nextLine().trim();
            try {
                int choice = Integer.parseInt(input);
                if (choice == 1) { encrypt = true; break; }
                if (choice == 2) { encrypt = false; break; }
            } catch (NumberFormatException ignored) {}
            Interfaces.errorMessageNumber();
        }

        String encodedContent = "";
        String publicContent = "";
        if (encrypt) {
            try {
                byte[] encryptedBytes = EncryptionUtil.encrypt(content, user.getUserKey());
                encodedContent = Base64.getEncoder().encodeToString(encryptedBytes);
            } catch (Exception e) {
                // handle encryption failure: notify user and fall back to plain content
                Interfaces.errorMessageEncryptionFailed();
                encrypt = false;
                publicContent = content;
            }
        } else {
            publicContent = content;
        }

        // For now let user pick a mood/location name from their lists or skip
        String mood = null;
        List<Moods> userMoodNames = listUserMoodNames(user);
        while (true) {
            for (int i = 0; i < userMoodNames.size(); i++) {

                System.out.println((i + 1) + ". " + userMoodNames.get(i).getMood());
            }
            System.out.println("|" + (userMoodNames.size() + 1) + "|" + " Create new mood");

            Interfaces.messagePromptChooseMood();
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice >= 1 && choice <= userMoodNames.size()) {
                mood = userMoodNames.get(choice - 1).getMood();
                break;
            } else if (choice == userMoodNames.size() + 1) {
                Interfaces.messagePromptNewMood();
                String newMoodName = scanner.nextLine().trim();
                createMood(user, newMoodName, mapper, moodFile);
                mood = newMoodName;
                break;
            } else {
                Interfaces.errorMessageNumber();
            }
        }

        String location = null;
        List<Locations> userLocationNames = listUserLocationNames(user);
        while (true) {
            for (int i = 0; i < userLocationNames.size(); i++) {

                System.out.println((i + 1) + ". " + userLocationNames.get(i).getLocation());
            }
            System.out.println("|" + (userLocationNames.size() + 1) + "|" + " Create new location");
            Interfaces.messagePromptChooseLocation();
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice >= 1 && choice <= userLocationNames.size()) {
                location = userLocationNames.get(choice - 1).getLocation();
                break;
            } else if (choice == userLocationNames.size() + 1) {
                Interfaces.messagePromptNewLocation();
                String newLocationName = scanner.nextLine().trim();
                createLocation(user, newLocationName, mapper, locationFile);
                location = newLocationName;
                break;
            } else {
                Interfaces.errorMessageNumber();
            }
        }

        DiaryEntry newEntry = new DiaryEntry(id, author, title, now, mood, location, encodedContent, publicContent, encrypt);
        entries.add(newEntry);
        try {
            mapper.writeValue(entryFile, getEntries());
        } catch (Exception e) {
            Interfaces.errorMessageUnableToWrite();
        }
        DiaryRead.clearConsole();
        Interfaces.currentUser(user);
        Interfaces.showEntry(id, entries, user);
    }

    public void sortOptions(User user, Scanner scanner, File diaryFile) {
        while (true) {
            Interfaces.messagePromptSortOption();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    DiaryRead.IndexAfterDate(diaryFile, scanner, user);
                    return;
                case "2":
                    DiaryRead.IndexFromDateToDate(diaryFile, scanner, user);
                    return;
                case "3":
                    if (user == null) {
                        DiaryRead.showIndex(diaryFile);
                    } else{
                        DiaryRead.myIndex(user, diaryFile);
                    }
                    return;
                case "4":
                    return;
                default:
                    Interfaces.errorMessageNumber();
            }
        }
    }

    /** 
     * Create a new mood.
     * @param author User creating the mood.
     * @param moodName Name of the new mood.
     * @param mapper ObjectMapper for JSON operations.
     * @param moodFile File to write updated moods to.
    */
    public void createMood (User author, String moodName, ObjectMapper mapper, File moodFile) {
        Moods newMood = new Moods(author.getUserName(), moodName);
        moods.add(newMood);
        try {
            mapper.writeValue(moodFile, getMoods());
        } catch (Exception e) {
            Interfaces.errorMessageUnableToWrite();
        }
    }

    /** 
     * Create a new location.
     * @param author User creating the location.
     * @param locationName Name of the new location.
     * @param mapper ObjectMapper for JSON operations.
     * @param locationFile File to write updated locations to.
    */
    public void createLocation (User author, String locationName, ObjectMapper mapper, File locationFile) {
        Locations newLocation = new Locations(author.getUserName(), locationName);
        locations.add(newLocation);
        try {
            mapper.writeValue(locationFile, getLocations());
        } catch (Exception e) {
            Interfaces.errorMessageUnableToWrite();
        }
    }

    /** 
     * Lists user-specific mood names.
     * @param user User whose moods are to be listed.
     * @return List of Moods created by the user.
    */
    public List<Moods> listUserMoodNames (User user) {
        ArrayList<Moods> userMoodNames = new ArrayList<>();
        for (Moods m : moods) {
            if (m.getCreator().equals(user.getUserName())) {
                userMoodNames.add(m);
            }
        }
        return userMoodNames;
    }

    /** 
     * Lists user-specific location names.
     * @param user User whose locations are to be listed.
     * @return List of Locations created by the user.
    */
    public ArrayList<Locations> listUserLocationNames (User user) {
        ArrayList<Locations> newLocations = new ArrayList<>();
        for (Locations l : locations) {
            if (l.getCreator().equals(user.getUserName())) {
                newLocations.add(l);
            }
        }
        return newLocations;
    }

    /**     
     * Retrieves the list of diary entries.
     * @return List of diary entries as an ArrayList.
    */
    public List<DiaryEntry> getEntries() {
        return entries;
    }

    /**     
     * Retrieves the list of locations.
     * @return List of locations as an ArrayList.
    */
    public List<Locations> getLocations() {
        return locations; 
    }

    /**     
     * Retrieves the list of moods.
     * @return List of moods as an ArrayList.
    */
    public List<Moods> getMoods() { 
        return moods; 
    }
}