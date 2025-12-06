package com.diary.util;

import java.io.File;
import java.util.Scanner;
import java.time.LocalDate;

import com.diary.model.User;
import com.diary.model.Moods;
import com.diary.model.Locations;
import com.diary.model.DiaryEntry;
import com.diary.manager.UserManager;

import com.fasterxml.jackson.databind.ObjectMapper;

public class UnitTests {
    /**
     * A test to show that a User can be created
     * Only requirement is that all fields are Strings
     * @param scanner
     * @param mapper
     * @param userFile
     */ 
    public void TestUserCreation(Scanner scanner, ObjectMapper mapper, File userFile) {
        User Bob = new User("6a8b2c4c-8a4d-4273-9433-3bf883197d79", "Bob", "Bobb@mail.no", "12345678", "a big man", "E/pixbdSr2VQx8Soq/A5s7wkyP1zSj1u9UUx2g7pHd0=", "VfqgUXO+fjWA8r8Ri5RtbFKKi5Fxv/VXcd8xN3UV0XI=");
        try {
            Interfaces.currentUser(Bob);
            Interfaces.messageUserCreationTestSuccess();
        } catch (Exception e) {
            Interfaces.errorMessageUserCreationTestFailed();
            System.exit(0);
        }
    }

    /**
     * A test to show that Users content fields do not need to have Specific properties
     * Phone number with letters, invalid email, weak password, special characters in description with no character limit.
     * No Password, Id and Key means bad security and possible encryption failiure.
     * @param scanner
     * @param mapper
     * @param userFile
     */
    public static void TestBadUserCreation(Scanner scanner, ObjectMapper mapper, File userFile) {
        User Bob = new User("", "Bob", "87654321", "bokstaver", "æøåæøåæølopkpiokpppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppp", "", "");
        try {
            Interfaces.currentUser(Bob);
            Interfaces.messageUserCreationTestSuccess();
        } catch (Exception e) {
            Interfaces.errorMessageUserCreationTestFailed();
            System.exit(0);
        }
    }

    /**
     * A test to show that Moods can be created
     * Only requirement is that both fields are Strings
     * @param scanner
     * @param mapper
     * @param moodFile
     */
    public static void TestMoodCreation(String moodName, ObjectMapper mapper, File moodFile) {
        Moods happy = new Moods("Happy","Bob");
        Moods sad = new Moods("Sad","Per");
        try {
            mapper.writeValue(moodFile, happy);
            mapper.writeValue(moodFile, sad);
            Interfaces.messageMoodCreationTestSuccess();
        } catch (Exception e) {
            Interfaces.errorMessageMoodCreationTestFailed();
        }
    }

    /**
     * A test to show that Locations can be created
     * Only requirement is that both fields are Strings
     * @param scanner
     * @param mapper
     * @param locationFile
     */
    public static void TestLocationCreation(Scanner scanner, ObjectMapper mapper, File locationFile) {
        Locations home = new Locations("Home","Bob");
        Locations work = new Locations("Work","Per");
        try {
            mapper.writeValue(locationFile, home);
            mapper.writeValue(locationFile, work);
            Interfaces.messageLocationCreationTestSuccess();
        } catch (Exception e) {
            Interfaces.errorMessageLocationCreationTestFailed();
        }
    }

    /**
     * A test to show that Diary Entries can be created with encryption
     * @param scanner
     * @param mapper
     * @param diaryFile
     */
    public static void TestDiaryEntryCreationEncryption(Scanner scanner, ObjectMapper mapper, File diaryFile) {
        LocalDate now = LocalDate.now();
        DiaryEntry encryptedEntry = new DiaryEntry("1", "Bob", "Encrypted Entry", now, "Happy", "Home", "U2FsdGVkX1+5nVbWc3J3p3bG5vY2tldA==", null, true);
        try {
            mapper.writeValue(diaryFile, encryptedEntry);
            Interfaces.messageEntryCreationTestSuccess();
        } catch (Exception e) {
            Interfaces.errorMessageEntryCreationTestFailed();
        }
    }

    /**
     * A test to show that Diary Entries can be created without encryption
     * @param scanner
     * @param mapper
     * @param diaryFile
     */
    public static void TestDiaryEntryCreationNoEncryption(Scanner scanner, ObjectMapper mapper, File diaryFile) {
        LocalDate now = LocalDate.now();
        DiaryEntry partialEncryptedEntry = new DiaryEntry("2", "Per", "Partially Encrypted Entry", now, "Sad", "Work", "", "This is public content", true);
        try {
            mapper.writeValue(diaryFile, partialEncryptedEntry);
            Interfaces.messageEntryCreationTestSuccess();
        } catch (Exception e) {
            Interfaces.errorMessageEntryCreationTestFailed();
        }
    }

    /**
     * A test to show that Diary Entries can be created with invalid content fields
     * @param scanner
     * @param mapper
     * @param diaryFile
     */
    public static void TestBadDiaryEntryCreation(Scanner scanner, ObjectMapper mapper, File diaryFile) {
        LocalDate now = LocalDate.now();
        DiaryEntry badEntry = new DiaryEntry("3", "random", "æpøæ'påøæø", now, "fedaddv1|12ddooiajdoi", "Srrweadasdawda", "ffsadfafaS>", "maby something like that", false);
        try {
            mapper.writeValue(diaryFile, badEntry);
            Interfaces.messageEntryCreationTestSuccess();
        } catch (Exception e) {
            Interfaces.errorMessageEntryCreationTestFailed();
        }
    }
}