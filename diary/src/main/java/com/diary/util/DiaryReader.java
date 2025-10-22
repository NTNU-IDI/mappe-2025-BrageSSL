package com.diary.util;

import com.diary.model.DiaryEntry;
import com.diary.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.util.Base64;
import java.util.List;

public class DiaryReader {

    public static void readDiary(User user, File diaryFile) {
        try {
            if (!diaryFile.exists() || diaryFile.length() == 0) {
                System.out.println("No diary entries found.");
                return;
            }

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            
            List<DiaryEntry> diaryList = mapper.readValue(diaryFile, new TypeReference<List<DiaryEntry>>() {});

            System.out.println("=== Diary Entries ===");
            for (DiaryEntry entry : diaryList) {
                if (!entry.getAuthor().equals(user.getUserName())) continue; // only show this user's entries

                byte[] encrypted = Base64.getDecoder().decode(entry.getEncodedContent());
                String content = EncryptionUtil.decrypt(encrypted, user.getUserKey());

                System.out.println("Title: " + entry.getTitle());
                System.out.println("Date: " + entry.getDate());
                System.out.println("Mood: " + entry.getMood());
                System.out.println("Location: " + entry.getLocation());
                System.out.println("Content: " + content);
                System.out.println("------------------------------");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to read diary entries.");
        }
    }
}