package com.diary.manager;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.UUID;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.diary.model.DiaryEntry;
import com.diary.model.Locations;
import com.diary.model.Moods;
import com.diary.util.EncryptionUtil;
import com.diary.util.Interfaces;
import com.diary.model.User;

public class EntryManager {
    private final List<DiaryEntry> entries = new ArrayList<>();

    public EntryManager() {
        // optionally load entries/moods/locations here using DiaryManager if you want caching
        try {
            List<DiaryEntry> loaded = DiaryManager.loadEntries(new File("diary/data/diary.json"), createMapper());
            if (loaded != null) entries.addAll(loaded);
        } catch (Exception ignored) {}
    }

    // Caller must pass shared Scanner from Main
    public void createEntry(User user, Scanner scanner) {
        String id = UUID.randomUUID().toString();
        String author = user.getUserName();
        LocalDateTime now = LocalDateTime.now().withSecond(0).withNano(0);

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
            System.out.println("---| Invalid input. Enter 1 or 2. |---");
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
        List<String> userMoodNames = listUserMoodNames(user);
        if (!userMoodNames.isEmpty()) {
            System.out.println("Choose mood by number or press Enter to skip:");
            for (int i = 0; i < userMoodNames.size(); i++) {
                System.out.println((i + 1) + ". " + userMoodNames.get(i));
            }
            String sel = scanner.nextLine().trim();
            if (!sel.isEmpty()) {
                try {
                    int idx = Integer.parseInt(sel) - 1;
                    if (idx >= 0 && idx < userMoodNames.size()) mood = userMoodNames.get(idx);
                } catch (NumberFormatException ignored) {}
            }
        }

        String location = null;
        List<String> userLocationNames = listUserLocationNames(user);
        if (!userLocationNames.isEmpty()) {
            System.out.println("Choose location by number or press Enter to skip:");
            for (int i = 0; i < userLocationNames.size(); i++) {
                System.out.println((i + 1) + ". " + userLocationNames.get(i));
            }
            String sel = scanner.nextLine().trim();
            if (!sel.isEmpty()) {
                try {
                    int idx = Integer.parseInt(sel) - 1;
                    if (idx >= 0 && idx < userLocationNames.size()) location = userLocationNames.get(idx);
                } catch (NumberFormatException ignored) {}
            }
        }

        DiaryEntry newEntry = new DiaryEntry(id, author, title, now, now, mood, location, encodedContent, publicContent, encrypt);
        entries.add(newEntry);

        // show entry — pass id, the entries list and the user
        Interfaces.showEntry(id, entries, user);
    }

    private ObjectMapper createMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        return mapper;
    }

    // Use DiaryManager.loadMood(File, ObjectMapper)
    public static List<Moods> loadMood(File fileName, ObjectMapper mapper) throws IOException {
        if (fileName == null || !fileName.exists() || fileName.length() == 0) {
            return Collections.emptyList();
        }
        Moods[] arr = mapper.readValue(fileName, Moods[].class);
        return Arrays.asList(arr);
    }

    // Load locations from file as List<Locations>
    public static List<Locations> loadLocation(File fileName, ObjectMapper mapper) throws IOException {
        if (fileName == null || !fileName.exists() || fileName.length() == 0) {
            return Collections.emptyList();
        }
        Locations[] arr = mapper.readValue(fileName, Locations[].class);
        return Arrays.asList(arr);
    }

    // Add these two methods to return names for the given user
    public List<String> listUserMoodNames(User user) {
        File moodFile = new File("diary/data/mood.json");
        List<Moods> all;
        try {
            all = loadMood(moodFile, createMapper()); // calls the static loader already in this class
        } catch (IOException e) {
            return Collections.emptyList();
        }
        return all.stream()
                  .filter(Objects::nonNull)
                  .filter(m -> user.getUserName().equals(m.getCreator()))
                  .map(Moods::getMood)
                  .collect(Collectors.toList());
    }

    public List<String> listUserLocationNames(User user) {
        File locFile = new File("diary/data/location.json");
        List<Locations> all;
        try {
            all = loadLocation(locFile, createMapper());
        } catch (IOException e) {
            return Collections.emptyList();
        }
        return all.stream()
                  .filter(Objects::nonNull)
                  .filter(l -> user.getUserName().equals(l.getCreator()))
                  .map(Locations::getLocation)
                  .collect(Collectors.toList());
    }

    // optional getter
    public List<DiaryEntry> getEntries() {
        return entries;
    }
}