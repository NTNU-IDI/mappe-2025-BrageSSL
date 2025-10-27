package com.diary.util;

import java.util.List;

import com.diary.model.DiaryEntry;
import com.diary.model.User;

public class Interfaces {
    public static void showEntry(String entryId, List<DiaryEntry> entries, User user) {

        DiaryEntry entry = entries.stream()
                .filter(e -> e.getId().equals(entryId))
                .findFirst()
                .orElse(null);

        if (entry == null) {
            System.out.println("---| No entry found with ID: " + entryId + " |---");
            return;
        }
        System.out.println("~~~~~~ Diary Entry Details ~~~~~~~");
        System.out.println("| Title:      " + entry.getTitle());
        System.out.println("| Author:     " + entry.getAuthor());
        System.out.println("| Date:       " + entry.getDate());
        System.out.println("| Mood:       " + entry.getMood());
        System.out.println("| Location:   " + entry.getLocation());
        if (entry.getEncrypted()) {
            try {
                String decrypted = EncryptionUtil.decrypt(entry.getEncryptedContent(), user.getUserKey());
                System.out.println("Content:\n" + decrypted);
            } catch (Exception e) {
                System.out.println("---| Content: [ENCRYPTED - unable to decrypt] |---");
            }

        } else {
            System.out.println("Content:\n" + entry.getPublicContent());
        }
        System.out.println("================================");
    }

    public static void showOtherEntry(String entryId, List<DiaryEntry> entries) {

        DiaryEntry entry = entries.stream()
                .filter(e -> e.getId().equals(entryId))
                .findFirst()
                .orElse(null);

        if (entry == null) {
            System.out.println("---| No entry found with ID: " + entryId + " |---");
            return;
        }
        System.out.println("~~~~~~ Diary Entry Details ~~~~~~");
        System.out.println("| Title:      " + entry.getTitle());
        System.out.println("| Date:       " + entry.getDate());
        System.out.println("| Mood:       " + entry.getMood());
        System.out.println("| Location:   " + entry.getLocation());
        if (entry.getEncrypted()){}
        else {System.out.println("| Content: " + entry.getPublicContent());}
        System.out.println("================================");
    }

    public static void showIndexEntry(String entryId, List<DiaryEntry> entries) {

        DiaryEntry entry = entries.stream()
                .filter(e -> e.getId().equals(entryId))
                .findFirst()
                .orElse(null);

        if (entry == null) {
            System.out.println("---| No entry found with ID: " + entryId + " |---");
            return;
        }
        System.out.println("~~~~~~ Diary Entry Details ~~~~~~");
        System.out.println("| Author: " + entry.getAuthor());
        System.out.println("| Title:  " + entry.getTitle());
        System.out.println("| Date:   " + entry.getDate());
        System.out.println("================================");
    }

    public static void menuOptions() {
        System.out.println("\n\n\n~~~~~~| What do you want to do? |~~~~~~");
        System.out.println("|1| Create new diary entry");
        System.out.println("|2| See Diary Index");
        System.out.println("|3| See Your diaries");
        System.out.println("|4| See other author's diaries");
        System.out.println("|5| Logout");
        System.out.println("|6| Exit");
        System.out.print  ("|~| ");
    }
    public static void loginMenu() {
        System.out.println("\n\n\n~~~~~~| Welcome to the Diary Application |~~~~~~");
        System.out.println("|1| Login");
        System.out.println("|2| Register");
        System.out.println("|3| Exit");
        System.out.print  ("|~| ");
    }
    public static void encryptionMenu() {
        System.out.println("\n\n\n~~~~~~| Encryption Options |~~~~~~");
        System.out.println("|1| Encrypt diary entries");
        System.out.println("|2| Do not encrypt diary entries");
        System.out.print  ("|~| ");
    }
    
}
