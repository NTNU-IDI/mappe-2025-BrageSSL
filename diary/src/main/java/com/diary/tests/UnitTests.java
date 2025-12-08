package com.diary.tests;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Base64;

import com.diary.model.User;
import com.diary.model.Moods;
import com.diary.model.Locations;
import com.diary.model.DiaryEntry;
import com.diary.manager.UserManager;
import com.diary.manager.DiaryManager;
import com.diary.manager.EntryManager;
import com.diary.util.EncryptionUtil;
import com.diary.util.Interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;

public class UnitTests {
    /**
     * A test to show that a User can be created
     * Only requirement is that all fields are Strings
     * 
     * @param scanner
     * @param mapper
     * @param userFile
     */
    public static void TestUserCreation(ObjectMapper mapper, File userFile) {
        ArrayList<User> users = DiaryManager.loadUser(userFile, mapper);
        User Bob = new User("6a8b2c4c-8a4d-4273-9433-3bf883197d79", "Bob", "Bobb@mail.no", "12345678", "a big man",
                "E/pixbdSr2VQx8Soq/A5s7wkyP1zSj1u9UUx2g7pHd0=", "VfqgUXO+fjWA8r8Ri5RtbFKKi5Fxv/VXcd8xN3UV0XI=");
        users.add(Bob);
        try {
            Interfaces.currentUser(Bob);
            mapper.writeValue(userFile, users);
            Interfaces.messageUserCreationTestSuccess();
        } catch (Exception e) {
            Interfaces.errorMessageUserCreationTestFailed();
        }
    }

    /**
     * A test to show that Users content fields do not need to have Specific
     * properties
     * Phone number with letters, invalid email, weak password, special characters
     * in description with no character limit.
     * No Password, Id and Key means bad security and possible encryption failiure.
     * 
     * @param scanner
     * @param mapper
     * @param userFile
     */
    public static void TestBadUserCreation(ObjectMapper mapper, File userFile) {
        ArrayList<User> users = DiaryManager.loadUser(userFile, mapper);
        try {
            User Per = new User("", "per", "87654321", "bokstaver",
                    "æøåæøåæølopkpiokpppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppp",
                    "", "");
            users.add(Per);
            Interfaces.currentUser(Per);
            mapper.writeValue(userFile, users);
            Interfaces.errorMessageBadUserCreationTestFailed();
        } catch (Exception e) {
            Interfaces.messageBadUserCreationTestSuccess();

        }
    }

    /**
     * A test to show that a User can be found by their username
     */
    public static void testFindUserByName(File userFile, ObjectMapper mapper) {
        ArrayList<User> Users = DiaryManager.loadUser(userFile, mapper);
        UserManager userManager = new UserManager(Users);
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
     * 
     * @param mapper
     * @param userFile
     */
    public static void testUserInfoEdit(ObjectMapper mapper, File userFile) {
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
     * 
     * @param scanner
     * @param mapper
     * @param moodFile
     */
    public static void TestMoodCreation(ObjectMapper mapper, File moodFile) {
        ArrayList<Moods> moods = DiaryManager.loadMood(moodFile, mapper);

        Moods happy = new Moods("Happy", "Bob");
        Moods sad = new Moods("Sad", "Per");
        moods.add(happy);
        moods.add(sad);
        try {
            mapper.writeValue(moodFile, moods);
            Interfaces.messageMoodCreationTestSuccess();
        } catch (Exception e) {
            Interfaces.errorMessageMoodCreationTestFailed();
        }
    }

    /**
     * A test to show that Moods can be found for a specific user
     * 
     * @param mapper
     * @param moodFile
     */
    public static void testFindUserMoods(ObjectMapper mapper, File moodFile, File userFile) {
        ArrayList<Moods> moods = DiaryManager.loadMood(moodFile, mapper);
        ArrayList<User> users = DiaryManager.loadUser(userFile, mapper);
        EntryManager entryManager = new EntryManager(null, null, moods);
        UserManager userManager = new UserManager(users);
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
     * 
     * @param mapper
     * @param moodFile
     */
    public static void testFindUserLocation(ObjectMapper mapper, File moodFile, File userFile) {
        ArrayList<User> users = DiaryManager.loadUser(userFile, mapper);
        ArrayList<Locations> locations = DiaryManager.loadLocations(moodFile, mapper);
        EntryManager entryManager = new EntryManager(null, locations, null);
        UserManager userManager = new UserManager(users);
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
     * 
     * @param scanner
     * @param mapper
     * @param locationFile
     */
    public static void TestLocationCreation(ObjectMapper mapper, File locationFile) {
        ArrayList<Locations> locations = DiaryManager.loadLocations(locationFile, mapper);
        Locations home = new Locations("Home", "Bob");
        Locations work = new Locations("Work", "Per");
        locations.add(home);
        locations.add(work);

        try {
            mapper.writeValue(locationFile, locations);
            Interfaces.messageLocationCreationTestSuccess();
        } catch (Exception e) {
            Interfaces.errorMessageLocationCreationTestFailed();
        }
    }

    /**
     * A test to show that Diary Entries can be created with encryption
     * 
     * @param scanner
     * @param mapper
     * @param diaryFile
     */
    public static void TestDiaryEntryCreationEncryption(ObjectMapper mapper, File diaryFile, File userFile) {
        ArrayList<User> users = DiaryManager.loadUser(userFile, mapper);
        ArrayList<DiaryEntry> entries = DiaryManager.loadEntries(diaryFile, mapper);
        UserManager userManager = new UserManager(users);
        LocalDateTime now = LocalDateTime.now().withSecond(0).withNano(0);
        LocalDateTime edited = LocalDateTime.now().withSecond(0).withNano(0);
        User user = userManager.findUser("Bob");
        String content = "This is some secret content that needs to be encrypted.";
        boolean encrypt = true;
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

        DiaryEntry encryptedEntry = new DiaryEntry("1", "Bob", "Encrypted Entry", now, edited, "Happy", "Home",
                encodedContent, publicContent, encrypt);
        DiaryEntry encryptedEntry2 = new DiaryEntry("2", "Bob", "Encrypted Entry2", now, edited, "Happy2", "Home2",
                encodedContent, publicContent, encrypt);
        entries.add(encryptedEntry);
        entries.add(encryptedEntry2);
        try {
            mapper.writeValue(diaryFile, entries);
            Interfaces.messageEntryCreationTestSuccess();
        } catch (Exception e) {
            Interfaces.errorMessageEntryCreationTestFailed();
        }
    }

    /**
     * A test to show that Diary Entries can be created without encryption
     * 
     * @param scanner
     * @param mapper
     * @param diaryFile
     */
    public static void TestDiaryEntryCreationNoEncryption(ObjectMapper mapper, File diaryFile, File userFile) {
        ArrayList<DiaryEntry> entries = DiaryManager.loadEntries(diaryFile, mapper);

        LocalDateTime now = LocalDateTime.now().withSecond(0).withNano(0);
        LocalDateTime edited = LocalDateTime.now().withSecond(0).withNano(0);
        DiaryEntry noEncryption = new DiaryEntry("3", "Bob", "Partially Encrypted Entry", now, edited, "Sad", "Work",
                "", "This is public content", false);
        entries.add(noEncryption);
        try {

            mapper.writeValue(diaryFile, entries);
            Interfaces.messageEntryCreationTestSuccess();
        } catch (Exception e) {
            Interfaces.errorMessageEntryCreationTestFailed();
        }
    }

    /**
     * A test to show that Diary Entries can be created with invalid content fields
     * this will make reading its content either fail or produce wierd results
     * 
     * @param scanner
     * @param mapper
     * @param diaryFile
     */
    public static void TestBadDiaryEntryCreation(ObjectMapper mapper, File diaryFile, File userFile) {
        ArrayList<DiaryEntry> entries = DiaryManager.loadEntries(diaryFile, mapper);
        ArrayList<User> users = DiaryManager.loadUser(userFile, mapper);
        UserManager userManager = new UserManager(users);
        LocalDateTime now = LocalDateTime.now().withSecond(0).withNano(0);
        LocalDateTime edited = LocalDateTime.now().withSecond(0).withNano(0);

        try {
            DiaryEntry badEntry = new DiaryEntry("4", "random", "æpøæ'påøæø", now, edited, "fedaddv1|12ddooiajdoi",
                    "Srrweadasdawda", "text", "maby something like that", false);
            entries.add(badEntry);
            mapper.writeValue(diaryFile, entries);
            Interfaces.errorMessageEntryCreationTestFailed();
            Interfaces.messageNoProperFieldInputCheck();
        } catch (Exception e) {
            Interfaces.messageEntryCreationTestSuccess();
        }
    }

    /**
     * A test to show that a Diary Entry can be retrieved by its ID
     * 
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
     * 
     * @param mapper
     * @param diaryFile
     */
    public static void testDeleteDiaryEntryById(ObjectMapper mapper, File diaryFile, File userFile) {
        ArrayList<DiaryEntry> entries = DiaryManager.loadEntries(diaryFile, mapper);
        ArrayList<User> users = DiaryManager.loadUser(userFile, mapper);
        EntryManager entryManager = new EntryManager(entries, null, null);
        UserManager userManager = new UserManager();
        User user = userManager.findUser("Bob");

        try {
            entries.removeIf(entryManager.getEntryById("2")::equals);
            mapper.writeValue(diaryFile, entries);
            Interfaces.messageEntryDeleted();
        } catch (Exception e) {
            Interfaces.errorMessageUnableToDeleteEntry();
        }
    }

    /**
     * A test to show that a Diary Entry can be edited by its ID
     * 
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

    /**
     * Run all unit tests.
     * 
     * @param mapper
     * @param userFile
     * @param moodFile
     * @param locationFile
     * @param diaryFile
     */
    public static void runAllTests(ObjectMapper mapper, File userFile, File moodFile, File locationFile,
            File diaryFile) {
        TestUserCreation(mapper, userFile);
        TestBadUserCreation(mapper, userFile);
        testFindUserByName(userFile, mapper);
        testUserInfoEdit(mapper, userFile);
        TestMoodCreation(mapper, moodFile);
        testFindUserMoods(mapper, moodFile, userFile);
        TestLocationCreation(mapper, locationFile);
        testFindUserLocation(mapper, locationFile, userFile);
        TestDiaryEntryCreationEncryption(mapper, diaryFile, userFile);
        TestDiaryEntryCreationNoEncryption(mapper, diaryFile, userFile);
        TestBadDiaryEntryCreation(mapper, diaryFile, userFile);
        getEntryByIdTest(mapper, diaryFile);
        testEditEntryById(mapper, diaryFile);
        testDeleteDiaryEntryById(mapper, diaryFile, userFile);
    }
}