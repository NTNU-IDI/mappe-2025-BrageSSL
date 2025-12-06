package com.diary.util;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;


import com.diary.model.DiaryEntry;
import com.diary.model.User;
import com.diary.util.Interfaces;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class DiaryRead {

    /** 
     * Show index of all diary entries.
     * @param diaryFile File containing diary entries.
    */
    public static void showIndex(File diaryFile) {
        clearConsole();        
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
        clearConsole();
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
        clearConsole();
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

    public static void IndexFromDateToDate(File diaryFile, Scanner scanner, User user) {
        clearConsole();
        LocalDate from;
        LocalDate to;
        
        try {
            if (!diaryFile.exists() || diaryFile.length() == 0) {
                Interfaces.errorMessageNoDiaryEntriesFound();
                return;
            }

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            
            List<DiaryEntry> diaryList = mapper.readValue(diaryFile, new TypeReference<List<DiaryEntry>>() {});

            clearConsole();

            while (true) {
                Interfaces.messagePromptDate();
                String fromDate = scanner.nextLine().trim();
                Interfaces.messagePromptDate();
                String toDate = scanner.nextLine().trim();

                try {
                    from = LocalDate.parse(fromDate);
                    to = LocalDate.parse(toDate);

                } catch (Exception e) {
                    Interfaces.errorMessageInvalidDateFormat();
                    continue;
                }
                break;
            }

            if (user == null) {
                for (DiaryEntry entry : diaryList) {
                    if (entry.getDate().isBefore(from) || entry.getDate().isAfter(to)) continue;
                    Interfaces.showOtherEntry(entry.getId(), diaryList);
                }
            } else {
                Interfaces.messageDiaryEntries();
                for (DiaryEntry entry : diaryList) {
                    if (!entry.getAuthor().equals(user.getUserName())) continue;
                    if (entry.getDate().isBefore(from) || entry.getDate().isAfter(to)) continue;
                    Interfaces.showOtherEntry(entry.getId(), diaryList);
                }
            }
        } catch (Exception e) {
            Interfaces.errorMessageFailedToReadEntries();
        }
    }

    public static void IndexAfterDate(File diaryFile, Scanner scanner, User user) {
        clearConsole();
        LocalDate date;
        
        try {
            if (!diaryFile.exists() || diaryFile.length() == 0) {
                Interfaces.errorMessageNoDiaryEntriesFound();
                return;
            }

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            
            List<DiaryEntry> diaryList = mapper.readValue(diaryFile, new TypeReference<List<DiaryEntry>>() {});

            clearConsole();

            while (true) {
                Interfaces.messagePromptDate();
                String atdate = scanner.nextLine().trim();

                try {
                    date = LocalDate.parse(atdate);
                } catch (Exception e) {
                    Interfaces.errorMessageInvalidDateFormat();
                    continue;
                }
                break;
            }

            if (user == null) {
                Interfaces.messageDiaryEntries();
                for (DiaryEntry entry : diaryList) {
                        if (!entry.getDate().isAfter(date)) continue;
                        Interfaces.showOtherEntry(entry.getId(), diaryList);
                }
            } else {
                Interfaces.messageDiaryEntries();
                for (DiaryEntry entry : diaryList) {
                    if (!entry.getAuthor().equals(user.getUserName())) continue;
                        if (!entry.getDate().isAfter(date)) continue;
                        Interfaces.showOtherEntry(entry.getId(), diaryList);
                }
            }
        } catch (Exception e) {
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