package com.diary.util;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import com.diary.manager.DiaryManager;
import com.diary.model.DiaryEntry;
import com.diary.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Utility class for reading and displaying diary entries.
 */
public class DiaryRead {

    /**
     * Show index of all diary entries.
     * 
     * @param diaryFile File containing diary entries.
     */
    public static void showIndex(User user, File diaryFile) {
        try {
            if (!diaryFile.exists() || diaryFile.length() == 0) {
                Interfaces.errorMessageNoDiaryEntriesFound();
                return;
            }

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());

            List<DiaryEntry> diaryList = mapper.readValue(diaryFile, new TypeReference<List<DiaryEntry>>() {
            });

            Interfaces.clearPlusUser(user);

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
     * 
     * @param user      User whose entries to show.
     * @param diaryFile File containing diary entries.
     */
    public static void myIndex(User user, File diaryFile) {
        try {
            if (!diaryFile.exists() || diaryFile.length() == 0) {
                Interfaces.errorMessageNoDiaryEntriesFound();
                return;
            }

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());

            List<DiaryEntry> diaryList = mapper.readValue(diaryFile, new TypeReference<List<DiaryEntry>>() {
            });

            Interfaces.clearPlusUser(user);

            Interfaces.messageDiaryEntries();
            for (DiaryEntry entry : diaryList) {
                if (!entry.getAuthor().equals(user.getUserName()))
                    continue; // only show this user's entries

                Interfaces.showEntry(entry.getId(), diaryList, user);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Interfaces.errorMessageFailedToReadEntries();
        }
    }

    /**
     * Show index of diary entries for a specific user.
     * 
     * @param user      User whose entries to show.
     * @param diaryFile File containing diary entries.
     */
    public static void otherIndex(User user, String userName, File diaryFile) {
        Interfaces.clearPlusUser(user);
        try {
            if (!diaryFile.exists() || diaryFile.length() == 0) {
                Interfaces.errorMessageNoDiaryEntriesFound();
                return;
            }

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());

            List<DiaryEntry> diaryList = mapper.readValue(diaryFile, new TypeReference<List<DiaryEntry>>() {
            });

            Interfaces.clearPlusUser(user);

            Interfaces.messageDiaryEntries();
            for (DiaryEntry entry : diaryList) {
                if (!entry.getAuthor().equals(userName))
                    continue; // only show this user's entries
                Interfaces.showOtherEntry(entry.getId(), diaryList);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Interfaces.errorMessageFailedToReadEntries();
        }
    }

    /**
     * Show index of diary entries between two dates.
     * 
     * @param diaryFile File containing diary entries.
     * @param scanner   Scanner for user input.
     * @param user      Current user (can be null).
     */
    public static void IndexFromDateToDate(File diaryFile, Scanner scanner, User user) {
        Interfaces.clearConsole();
        LocalDateTime from;
        LocalDateTime to;

        try {
            if (!diaryFile.exists() || diaryFile.length() == 0) {
                Interfaces.errorMessageNoDiaryEntriesFound();
                return;
            }

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());

            List<DiaryEntry> diaryList = mapper.readValue(diaryFile, new TypeReference<List<DiaryEntry>>() {
            });

            Interfaces.clearPlusUser(user);

            while (true) {
                Interfaces.messagePromptDate();
                String fromDate = scanner.nextLine().trim();
                Interfaces.messagePromptDate();
                String toDate = scanner.nextLine().trim();

                try {
                    from = LocalDateTime.parse(fromDate);
                    to = LocalDateTime.parse(toDate);

                } catch (Exception e) {
                    Interfaces.errorMessageInvalidDateFormat();
                    continue;
                }
                break;
            }

            if (user == null) {
                for (DiaryEntry entry : diaryList) {
                    if (entry.getDate().isBefore(from) || entry.getDate().isAfter(to))
                        continue;
                    Interfaces.showOtherEntry(entry.getId(), diaryList);
                }
            } else {
                Interfaces.messageDiaryEntries();
                for (DiaryEntry entry : diaryList) {
                    if (!entry.getAuthor().equals(user.getUserName()))
                        continue;
                    if (entry.getDate().isBefore(from) || entry.getDate().isAfter(to))
                        continue;
                    Interfaces.showOtherEntry(entry.getId(), diaryList);
                }
            }
        } catch (Exception e) {
            Interfaces.errorMessageFailedToReadEntries();
        }
    }

    /**
     * Show index of diary entries after a specific date.
     * 
     * @param diaryFile File containing diary entries.
     * @param scanner   Scanner for user input.
     * @param user      Current user (can be null).
     */
    public static void IndexAfterDate(File diaryFile, Scanner scanner, User user) {
        Interfaces.clearConsole();
        LocalDateTime date;

        try {
            if (!diaryFile.exists() || diaryFile.length() == 0) {
                Interfaces.errorMessageNoDiaryEntriesFound();
                return;
            }

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());

            List<DiaryEntry> diaryList = mapper.readValue(diaryFile, new TypeReference<List<DiaryEntry>>() {
            });

            Interfaces.clearConsole();

            while (true) {
                Interfaces.messagePromptDate();
                String atdate = scanner.nextLine().trim();

                try {
                    date = LocalDateTime.parse(atdate);
                } catch (Exception e) {
                    Interfaces.errorMessageInvalidDateFormat();
                    continue;
                }
                break;
            }

            if (user == null) {
                Interfaces.messageDiaryEntries();
                for (DiaryEntry entry : diaryList) {
                    if (!entry.getDate().isAfter(date))
                        continue;
                    Interfaces.showOtherEntry(entry.getId(), diaryList);
                }
            } else {
                Interfaces.messageDiaryEntries();
                for (DiaryEntry entry : diaryList) {
                    if (!entry.getAuthor().equals(user.getUserName()))
                        continue;
                    if (!entry.getDate().isAfter(date))
                        continue;
                    Interfaces.showOtherEntry(entry.getId(), diaryList);
                }
            }
        } catch (Exception e) {
            Interfaces.errorMessageFailedToReadEntries();
        }
    }

    /**
     * Show index of diary entries grouped by author.
     * 
     * @param diaryFile File containing diary entries.
     * @param userFile  File containing user data.
     * @param user      Current user.
     * @param mapper    ObjectMapper for JSON processing.
     */
    public static void authorIndex(File diaryFile, File userFile, User user, ObjectMapper mapper) {
        ArrayList<User> users = DiaryManager.loadUser(userFile, mapper);
        ArrayList<DiaryEntry> entries = DiaryManager.loadEntries(diaryFile, mapper);
        Interfaces.clearPlusUser(user);
        int entrycount = 0;
        try {
            if (!diaryFile.exists() || diaryFile.length() == 0) {
                Interfaces.errorMessageNoDiaryEntriesFound();
                return;
            }

            Interfaces.messageDiaryEntries();
            for (User u : users) {
                for (DiaryEntry entry : entries) {
                    if (!entry.getAuthor().equals(u.getUserName())) {
                        continue;
                    }
                    entrycount++;

                }
                Interfaces.showAuthorEntry(u.getUserName(), entrycount);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Interfaces.errorMessageFailedToReadEntries();
        }
    }
}