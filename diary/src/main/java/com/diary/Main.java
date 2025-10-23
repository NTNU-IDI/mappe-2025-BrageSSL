package com.diary;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.diary.model.DiaryEntry;
import com.diary.model.User;
import com.diary.util.EncryptionUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.enable(SerializationFeature.INDENT_OUTPUT);

            // ---------- Load user ----------
            File userFile = new File("data/user.json");
            if (!userFile.exists() || userFile.length() == 0) {
                System.out.println("No user found. Please create a new user first.");
                return;
            }
            User user = mapper.readValue(userFile, User.class);

            // Authenticate
            user.UserAuth(scanner);

            // ---------- Load diary entries ----------
            File diaryFile = new File("data/diary.json");
            List<DiaryEntry> entries = new ArrayList<>();
            if (diaryFile.exists() && diaryFile.length() > 0) {
                DiaryEntry[] loaded = mapper.readValue(diaryFile, DiaryEntry[].class);
                for (DiaryEntry e : loaded) entries.add(e);
            }

            // Ask user if they want to add a new entry
            System.out.print("Do you want to create a new diary entry? (y/n): ");
            String choice = scanner.nextLine().trim().toLowerCase();
            if (choice.equals("y")) {
                DiaryEntry newEntry = new DiaryEntry(user, scanner);
                entries.add(newEntry);

                // Save diary entries
                mapper.writeValue(diaryFile, entries);
                System.out.println("Diary entry saved successfully!");
            }

            // Display all diary entries
            System.out.println("\n--- Your Diary Entries ---");
            for (DiaryEntry entry : entries) {
                System.out.println("Title: " + entry.getTitle());
                System.out.println("Mood: " + entry.getMood());
                System.out.println("Location: " + entry.getLocation());
                System.out.println("Date: " + entry.getDate());
                String content = EncryptionUtil.decrypt(entry.getEncryptedContent(), user.getUserKey());
                System.out.println("Content: " + content);
                System.out.println("-------------------------");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}