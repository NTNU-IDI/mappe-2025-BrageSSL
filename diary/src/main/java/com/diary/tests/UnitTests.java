package com.diary.tests;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

import com.diary.model.User;
import com.diary.model.Moods;
import com.diary.model.Locations;
import com.diary.model.DiaryEntry;
import com.diary.manager.UserManager;
import com.diary.manager.DiaryManager;
import com.diary.manager.EntryManager;
import com.diary.util.Interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;

public class UnitTests {
    /**
     * A test to show that a User can be created
     * Only requirement is that all fields are Strings
     * @param scanner
     * @param mapper
     * @param userFile
     */ 
    public static  void TestUserCreation(ObjectMapper mapper, File userFile) {
        ArrayList<User> users = DiaryManager.loadUser(userFile, mapper);
        UserManager usersManager = new UserManager(users);
        User Bob = new User("6a8b2c4c-8a4d-4273-9433-3bf883197d79", "Bob", "Bobb@mail.no", "12345678", "a big man", "E/pixbdSr2VQx8Soq/A5s7wkyP1zSj1u9UUx2g7pHd0=", "VfqgUXO+fjWA8r8Ri5RtbFKKi5Fxv/VXcd8xN3UV0XI=");
        try {
            Interfaces.currentUser(Bob);
            mapper.writeValue(userFile, usersManager.getUsers());
            Interfaces.messageUserCreationTestSuccess();
        } catch (Exception e) {
            Interfaces.errorMessageUserCreationTestFailed();
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
    public static void TestBadUserCreation(ObjectMapper mapper, File userFile) {
        ArrayList<User> users = DiaryManager.loadUser(userFile, mapper);
        UserManager usersManager = new UserManager(users);
        try {
            User Per = new User("", "per", "87654321", "bokstaver", "æøåæøåæølopkpiokpppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppp", "", "");
            Interfaces.currentUser(Per);
            mapper.writeValue(userFile, usersManager.getUsers());
            Interfaces.errorMessageBadUserCreationTestFailed();
        } catch (Exception e) {
            Interfaces.messageBadUserCreationTestSuccess();
            
        }
    }

    /**
     * A test to show that a User can be found by their username
     */
    public static void testFindUserByName() {
        UserManager userManager = new UserManager();
        User foundValid = userManager.findUser("Bob");
        User foundInvalid = userManager.findUser("NonExistentUser");

        if (foundValid != null || foundInvalid == null) {
            Interfaces.messageUserFoundTestSuccess();
        } else {
            Interfaces.errorMessageUserFoundTestFailed();
        }
    }

    /**
     * A test to show that a User's info can be edited
     * @param mapper
     * @param userFile
     */
    public static void testUserInfoEdit(ObjectMapper mapper, File userFile){
        ArrayList<User> users = DiaryManager.loadUser(userFile, mapper);
        UserManager userManager = new UserManager(users);
        User user = userManager.findUser("Bob");
        if (user != null) {
            user.setEmail("newfakemail@example.com");
            user.setPhoneNumber("87654321");
            user.setDescription("Updated description");
            try {
                Interfaces.currentUser(user);
                mapper.writeValue(userFile, userManager.getUsers());
                Interfaces.messageUserCreationTestSuccess();
                
            } catch (Exception e) {
                Interfaces.errorMessageUserEditTestFailed();
            }
        } else {
            Interfaces.errorMessageUserEditTestFailedFast();
        }
    }

    /**
     * A test to show that Moods can be created
     * Only requirement is that both fields are Strings
     * @param scanner
     * @param mapper
     * @param moodFile
     */
    public static void TestMoodCreation(ObjectMapper mapper, File moodFile) {
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
     * A test to show that Moods can be found for a specific user
     * @param mapper
     * @param moodFile
     */
    public static void testFindUserMoods(ObjectMapper mapper, File moodFile) {
        ArrayList<Moods> moods = DiaryManager.loadMood(moodFile, mapper);
        EntryManager entryManager = new EntryManager(null, null, moods);
        UserManager userManager = new UserManager();
        User user = userManager.findUser("Bob");
        List<Moods> userMoodNames = entryManager.listUserMoodNames(user);
        if (userMoodNames.isEmpty()) {
            Interfaces.errorMessageNoMoodsFound();
            return;
        }
        for (int i = 0; i < userMoodNames.size(); i++) {
            System.out.println("|" + (i + 1) + "| " + userMoodNames.get(i).getMood());
        }
    }

    /** 
     * A test to show that Locations can be found for a specific user
     * @param mapper
     * @param moodFile
     */
    public static void testFindUserLocation(ObjectMapper mapper, File moodFile) {
        ArrayList<Locations> locations = DiaryManager.loadLocations(moodFile, mapper);
        EntryManager entryManager = new EntryManager(null, locations, null);
        UserManager userManager = new UserManager();
        User user = userManager.findUser("Bob");
        List<Locations> userLocationNames = entryManager.listUserLocationNames(user);
        if (userLocationNames.isEmpty()) {
            Interfaces.errorMessageNoLocationsFound();
            return;
        }
        for (int i = 0; i < userLocationNames.size(); i++) {
            System.out.println("|" + (i + 1) + "| " + userLocationNames.get(i).getLocation());
        }

    }

    /**
     * A test to show that Locations can be created
     * Only requirement is that both fields are Strings
     * @param scanner
     * @param mapper
     * @param locationFile
     */
    public static void TestLocationCreation(ObjectMapper mapper, File locationFile) {
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
    public static void TestDiaryEntryCreationEncryption(ObjectMapper mapper, File diaryFile) {
        LocalDateTime now = LocalDateTime.now().withSecond(0).withNano(0);
        LocalDateTime edited = LocalDateTime.now().withSecond(0).withNano(0);
        DiaryEntry encryptedEntry = new DiaryEntry("1", "Bob", "Encrypted Entry", now, edited, "Happy", "Home", "U2FsdGVkX1+5nVbWc3J3p3bG5vY2tldA==", null, true);
        DiaryEntry encryptedEntry2 = new DiaryEntry("2", "Bob", "Encrypted Entry2", now, edited, "Happy2", "Home2", "U2FsdGVkX1+5nVbWc3J3p3bG5vY2tldA==", null, true);
        try {
            mapper.writeValue(diaryFile, encryptedEntry);
            mapper.writeValue(diaryFile, encryptedEntry2);
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
    public static void TestDiaryEntryCreationNoEncryption(ObjectMapper mapper, File diaryFile) {
        LocalDateTime now = LocalDateTime.now().withSecond(0).withNano(0);
        LocalDateTime edited = LocalDateTime.now().withSecond(0).withNano(0);
        DiaryEntry partialEncryptedEntry = new DiaryEntry("3", "Per", "Partially Encrypted Entry", now, edited,"Sad", "Work", "", "This is public content", true);
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
    public static void TestBadDiaryEntryCreation(ObjectMapper mapper, File diaryFile) {
        LocalDateTime now = LocalDateTime.now().withSecond(0).withNano(0);
        LocalDateTime edited = LocalDateTime.now().withSecond(0).withNano(0);
        DiaryEntry badEntry = new DiaryEntry("4", "random", "æpøæ'påøæø", now, edited, "fedaddv1|12ddooiajdoi", "Srrweadasdawda", "ffsadfafaS>", "maby something like that", false);
        try {
            mapper.writeValue(diaryFile, badEntry);
            Interfaces.messageEntryCreationTestSuccess();
        } catch (Exception e) {
            Interfaces.errorMessageEntryCreationTestFailed();
        }
    }

    /** 
     * A test to show that a Diary Entry can be retrieved by its ID
     * @param mapper
     * @param diaryFile
     */
    public static void getEntryByIdTest(ObjectMapper mapper, File diaryFile) {
        ArrayList<DiaryEntry> entries = DiaryManager.loadEntries(diaryFile, mapper);
        EntryManager entryManager = new EntryManager(entries, null, null);
        UserManager userManager = new UserManager();
        User user = userManager.findUser("Bob");
        try {
            DiaryEntry entry = entryManager.getEntryById("1");
            if (entry != null) {
                Interfaces.showIndexEntry("3", entries);
                Interfaces.messageGetEntryByIdTestSuccess();
            } else {
                Interfaces.errorMessageGetEntryByIdTestFailed();
            }
        } catch (Exception e) {
            Interfaces.errorMessageGetEntryByIdTestFailed();
        }
    }

    /** 
     * A test to show that a Diary Entry can be deleted by its ID
     * @param mapper
     * @param diaryFile
     */
    public static void testDeleteDiaryEntryById(ObjectMapper mapper, File diaryFile) {
        ArrayList<DiaryEntry> entries = DiaryManager.loadEntries(diaryFile, mapper);
        EntryManager entryManager = new EntryManager(entries, null, null);
        UserManager userManager = new UserManager();
        User user = userManager.findUser("Bob");
        try {
            entryManager.deleteOwnEntryById(user, "2", mapper, diaryFile);
            Interfaces.messageEntryDeleted();
        } catch (Exception e) {
            Interfaces.errorMessageUnableToDeleteEntry();
        }
    }

    /** 
     * A test to show that a Diary Entry can be edited by its ID
     * @param mapper
     * @param diaryFile
     */
    public static void testEditEntryById(ObjectMapper mapper, File diaryFile) {
        ArrayList<DiaryEntry> entries = DiaryManager.loadEntries(diaryFile, mapper);
        EntryManager entryManager = new EntryManager(entries, null, null);
        DiaryEntry entry = entryManager.getEntryById("1");
        entry.setMood("Exited");
        entry.setLocation("New location!!");
        entry.setDateEdited(LocalDateTime.now().withSecond(0).withNano(0));
        try {
            mapper.writeValue(diaryFile, entryManager.getEntries());
        } catch (Exception e) {
            Interfaces.errorMessageUnableToWrite();
        }
    }

    public static void runAllTests(ObjectMapper mapper, File userFile, File moodFile, File locationFile, File diaryFile) {
        TestUserCreation(mapper, userFile);
        TestBadUserCreation(mapper, userFile);
        testFindUserByName();
        testUserInfoEdit(mapper, userFile);
        TestMoodCreation(mapper, moodFile);
        testFindUserMoods(mapper, moodFile);
        TestLocationCreation(mapper, locationFile);
        testFindUserLocation(mapper, locationFile);
        TestDiaryEntryCreationEncryption(mapper, diaryFile);
        TestDiaryEntryCreationNoEncryption(mapper, diaryFile);
        TestBadDiaryEntryCreation(mapper, diaryFile);
        getEntryByIdTest(mapper, diaryFile);
        testEditEntryById(mapper, diaryFile);
        testDeleteDiaryEntryById(mapper, diaryFile);
    }
}