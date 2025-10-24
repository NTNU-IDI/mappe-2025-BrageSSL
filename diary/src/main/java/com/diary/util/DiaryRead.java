package com.diary.util;

import java.io.File;
import java.util.List;

import com.diary.manager.DiaryManager;
import com.diary.model.DiaryEntry;
import com.diary.model.User;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class DiaryRead {

    public static void showIndex(File diaryFile) {
        DiaryRead.clearConsole();
        try {
            if (!diaryFile.exists() || diaryFile.length() == 0) {
                System.out.println("---| No diary entries found.|---");
                return;
            }

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            
            List<DiaryEntry> diaryList = mapper.readValue(diaryFile, new TypeReference<List<DiaryEntry>>() {});

            System.out.println("===== Diary Entries =====");
            for (DiaryEntry entry : diaryList) {
                DiaryManager.showIndexEntry(entry.getId(), diaryList);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("---| Failed to read diary entries. |---");
        }
    }
    public static void myIndex(User user, File diaryFile) {
        DiaryRead.clearConsole();
        try {
            if (!diaryFile.exists() || diaryFile.length() == 0) {
                System.out.println("---| No diary entries found. |---");
                return;
            }

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            
            List<DiaryEntry> diaryList = mapper.readValue(diaryFile, new TypeReference<List<DiaryEntry>>() {});

            System.out.println("===== Diary Entries =====");
            for (DiaryEntry entry : diaryList) {
                if (!entry.getAuthor().equals(user.getUserName())) continue; // only show this user's entries

                DiaryManager.showEntry(entry.getId(), diaryList, user);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("---| Failed to read diary entries. |---");
        }
    }
    public static void otherIndex(String user, File diaryFile) {
        DiaryRead.clearConsole();
        try {
            if (!diaryFile.exists() || diaryFile.length() == 0) {
                System.out.println("---| No diary entries found. |---");
                return;
            }

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            
            List<DiaryEntry> diaryList = mapper.readValue(diaryFile, new TypeReference<List<DiaryEntry>>() {});

            clearConsole();
            
            System.out.println("===== Diary Entries =====");
            for (DiaryEntry entry : diaryList) {
                if (!entry.getAuthor().equals(user)) continue; // only show this user's entries
                DiaryManager.showOtherEntry(entry.getId(), diaryList);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("---| Failed to read diary entries. |---");
        }
    }
    public static void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            System.out.println("---| Could not clear console |---");
        }
    }

}