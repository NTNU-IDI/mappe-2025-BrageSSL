package com.diary.manager;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
     * Entry manager Constructors.
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
        LocalDateTime made = LocalDateTime.now().withSecond(0).withNano(0);    
        LocalDateTime now = made.withSecond(0).withNano(0);

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

        List<Moods> userMoodNames = listUserMoodNames(user);
        String mood = listUserMoods(userMoodNames, user, scanner, mapper, moodFile);

        List<Locations> userLocationNames = listUserLocationNames(user);
        String location = listUserLocations(userLocationNames, user, scanner, mapper, locationFile);
        
        DiaryEntry newEntry = new DiaryEntry(id, author, title, made, now, mood, location, encodedContent, publicContent, encrypt);
        entries.add(newEntry);
        try {
            mapper.writeValue(entryFile, getEntries());
        } catch (Exception e) {
            Interfaces.errorMessageUnableToWrite();
        }
        Interfaces.clearPlusUser(user);
        Interfaces.showEntry(id, entries, user);
    }

    /** 
     * Display sorting options for diary entries.
     * @param user User whose entries are to be sorted.
     * @param scanner Scanner for user input.
     * @param diaryFile File containing diary entries.
    */
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
                        DiaryRead.showIndex(user, diaryFile);
                    } else {
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
        Moods newMood = new Moods(moodName, author.getUserName());
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
        Locations newLocation = new Locations(locationName, author.getUserName());
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
     * Lists moods for user selection or creation.
     * @param userMoodNames List of Moods created by the user.
     * @param user User selecting or creating a mood.
     * @param scanner Scanner for user input.
     * @param mapper ObjectMapper for JSON operations.
     * @param moodFile File to write updated moods to.
     * @return Selected or newly created mood as a String.
    */
    public String listUserMoods(List<Moods> userMoodNames, User user, Scanner scanner, ObjectMapper mapper, File moodFile) {
        String mood = null;
        while (true) {
            for (int i = 0; i < userMoodNames.size(); i++) {

                System.out.println("|" + (i + 1) + "| " + userMoodNames.get(i).getMood());
            }
            System.out.println("|" + (userMoodNames.size() + 1) + "|" + " Create new mood");

            Interfaces.messagePromptChooseMood();
            String Temp = scanner.nextLine().trim();
            int choice = Integer.parseInt(Temp);
            try {
                choice = Integer.parseInt(Temp);
            } catch (NumberFormatException e) {
                Interfaces.clearPlusUser(user);
                Interfaces.errorMessageNumber();
                continue;
            }

            if (choice >= 1 && choice <= userMoodNames.size()) {
                mood = userMoodNames.get(choice - 1).getMood();
                return mood;
            } else if (choice == userMoodNames.size() + 1) {
                Interfaces.messagePromptNewMood();
                String newMoodName = scanner.nextLine().trim();
                createMood(user, newMoodName, mapper, moodFile);
                mood = newMoodName;
                return mood;
            } else {
                Interfaces.errorMessageNumber();
            }
        }
    }

    /** 
     * Lists locations for user selection or creation.
     * @param userLocationNames List of Locations created by the user.
     * @param user User selecting or creating a location.
     * @param scanner Scanner for user input.
     * @param mapper ObjectMapper for JSON operations.
     * @param locationFile File to write updated locations to.
     * @return Selected or newly created location as a String.
    */
    public String listUserLocations(List<Locations> userLocationNames, User user, Scanner scanner, ObjectMapper mapper, File locationFile) {
        String location = null;
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
                return location;
            } else if (choice == userLocationNames.size() + 1) {
                Interfaces.messagePromptNewLocation();
                String newLocationName = scanner.nextLine().trim();
                createLocation(user, newLocationName, mapper, locationFile);
                location = newLocationName;
                return location;
            } else {
                Interfaces.errorMessageNumber();
            }
        }
    }

    /**     
     * Delete a diary entry by its ID if it belongs to the user.
     * @param user User attempting to delete the entry.
     * @param id ID of the diary entry to be deleted.
     * @param mapper ObjectMapper for JSON operations.
     * @param entryFile File to write updated entries to.
     * @return true if deletion was successful, false otherwise.
    */
    public boolean deleteOwnEntryById(User user, String id, ObjectMapper mapper, File entryFile) {
        if (user == null || id == null || id.isEmpty()) return false;
        boolean removed = entries.removeIf(e -> id.equals(e.getId()) && user.getUserName().equals(e.getAuthor()));
        if (!removed) return false;
        try {
            mapper.writeValue(entryFile, getEntries());
            return true;
        } catch (Exception e) {
            Interfaces.errorMessageUnableToWrite();
            return false;
        }
    }

    /**     
     * Update an existing diary entry.
     * @param user User updating the entry.
     * @param mapper ObjectMapper for JSON operations.
     * @param entryFile File to write updated entries to.
     * @param scanner Scanner for user input.
    */
    public void updateEntry(User user, ObjectMapper mapper, File entryFile, Scanner scanner) {
        boolean running = true;
        while (true) {
            running = true;
            Interfaces.clearPlusUser(user);

            Interfaces.messagePromptID();
            String id = scanner.nextLine().trim();
            DiaryEntry entry = getEntryById(id);

            if (entry == null) {
                Interfaces.errorMessageNoDiaryEntriesFound();
                return;
            }
            while(running) {
                Interfaces.clearPlusUser(user);
                Interfaces.showEntry(id, entries, user);
                Interfaces.messagePromptManageEntries();
                String choice = scanner.nextLine().trim();
                switch (choice) {
                    case "1":
                        Interfaces.messagePromptTitle();
                        String newTitle = scanner.nextLine().trim();
                        entry.setTitle(newTitle);
                        break;
                    case "2":
                        List<Moods> userMoodNames = listUserMoodNames(user);
                        String newMood = listUserMoods(userMoodNames, user, scanner, mapper, entryFile);
                        entry.setMood(newMood);
                        break;
                    case "3":
                        List<Locations> userLocationNames = listUserLocationNames(user);
                        String newLocation = listUserLocations(userLocationNames, user, scanner, mapper, entryFile);
                        entry.setLocation(newLocation);
                        break;
                    case "4":
                        Interfaces.messagePromptContent();
                        StringBuilder contentBuilder = new StringBuilder();
                        String line;
                        while (true) {
                            line = scanner.nextLine();
                            if (line.equalsIgnoreCase("END")) break;
                            contentBuilder.append(line).append(System.lineSeparator());
                        }
                        String newContent = contentBuilder.toString().trim();
                        if (entry.getEncrypted()) {
                            try {
                                byte[] encryptedBytes = EncryptionUtil.encrypt(newContent, user.getUserKey());
                                String encodedContent = Base64.getEncoder().encodeToString(encryptedBytes);
                                entry.setEncodedContent(encodedContent);
                            } catch (Exception e) {
                                Interfaces.errorMessageEncryptionFailed();
                            }
                        } else {
                            entry.setPublicContent(newContent);
                        }
                        break;
                    case "5":
                        boolean success = deleteOwnEntryById(user, id, mapper, entryFile);
                        if (success) {
                            Interfaces.messageEntryDeleted();
                        } else {
                            Interfaces.errorMessageUnableToWrite();
                        }
                        break;
                    case "6":
                        running = false;
                        break;
                    case "7":
                        return;
                    default:
                        Interfaces.errorMessageNumber();
                        continue;
                }
                entry.setDateEdited(LocalDateTime.now().withSecond(0).withNano(0));
                try {
                    mapper.writeValue(entryFile, getEntries());
                } catch (Exception e) {
                    Interfaces.errorMessageUnableToWrite();
                }
            }
            
        }
    }

    /**     
     * Retrieves a diary entry by its ID.
     * @param id ID of the diary entry.
     * @return DiaryEntry object if found, otherwise null.
    */
    public DiaryEntry getEntryById (String id) {
        for (DiaryEntry entry : entries) {
            if (id.equals(entry.getId())) {
                return entry;
            }
        }
        return null; // Entry not found
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