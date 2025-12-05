package com.diary.util;

import java.io.File;
import java.util.List;

import com.diary.model.DiaryEntry;
import com.diary.model.User;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class DiaryRead {

    /** 
     * Show index of all diary entries.
     * @param diaryFile File containing diary entries.
    */
    public static void showIndex(File diaryFile) {
        DiaryRead.clearConsole();
        try {
            if (!diaryFile.exists() || diaryFile.length() == 0) {
                Interfaces.errorMessageNoDiaryEntriesFound();
                return;
            }

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            
            List<DiaryEntry> diaryList = mapper.readValue(diaryFile, new TypeReference<List<DiaryEntry>>() {});

            Interfaces.messageDiaryEntries();
            for (DiaryEntry entry : diaryList) {
                Interfaces.showIndexEntry(entry.getId(), diaryList);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Interfaces.errorMessageFailedToReadEntries();
        }
    }
    
    /** 
     * Show index of diary entries for a specific user.
     * @param user User whose entries to show.
     * @param diaryFile File containing diary entries.
    */
    public static void myIndex(User user, File diaryFile) {
        DiaryRead.clearConsole();
        try {
            if (!diaryFile.exists() || diaryFile.length() == 0) {
                Interfaces.errorMessageNoDiaryEntriesFound();
                return;
            }

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            
            List<DiaryEntry> diaryList = mapper.readValue(diaryFile, new TypeReference<List<DiaryEntry>>() {});

            Interfaces.messageDiaryEntries();
            for (DiaryEntry entry : diaryList) {
                if (!entry.getAuthor().equals(user.getUserName())) continue; // only show this user's entries

                Interfaces.showEntry(entry.getId(), diaryList, user);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Interfaces.errorMessageFailedToReadEntries();
        }
    }

    /** 
     * Show index of diary entries for a specific user.
     * @param user User whose entries to show.
     * @param diaryFile File containing diary entries.
    */
    public static void otherIndex(String user, File diaryFile) {
        DiaryRead.clearConsole();
        try {
            if (!diaryFile.exists() || diaryFile.length() == 0) {
                Interfaces.errorMessageNoDiaryEntriesFound();
                return;
            }

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            
            List<DiaryEntry> diaryList = mapper.readValue(diaryFile, new TypeReference<List<DiaryEntry>>() {});

            clearConsole();
            
            Interfaces.messageDiaryEntries();
            for (DiaryEntry entry : diaryList) {
                if (!entry.getAuthor().equals(user)) continue; // only show this user's entries
                Interfaces.showOtherEntry(entry.getId(), diaryList);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Interfaces.errorMessageFailedToReadEntries();
        }
    }

    /** 
     * Clear the console screen.
    */
    public static void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            Interfaces.errorMessageUnableToClearConsole();
        }
    }

}