package com.diary.util;

import java.util.List;

import com.diary.model.DiaryEntry;
import com.diary.model.User;

public class Interfaces {
    /**
     * Display detailed diary entry information.
     * @param entryId
     * @param entries
     * @param user
     */
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
        System.out.println("| ID:         " + entry.getId());
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

    /** 
     * Display another author's diary entry information.
     * @param entryId
     * @param entries
     */
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

    /** 
     * Display another author's diary entry information.
     * @param entryId
     * @param entries
    */
    public static void showIndexEntry(String entryId, List<DiaryEntry> entries) {

        DiaryEntry entry = entries.stream()
                .filter(e -> e.getId().equals(entryId))
                .findFirst()
                .orElse(null);

        if (entry == null) {
            System.out.println("---| No entry found with ID: " + entryId + " |---");
            return;
        }
        System.out.println("\n\n\n~~~~~~ Diary Entry Details ~~~~~~");
        System.out.println("| Author: " + entry.getAuthor());
        System.out.println("| Title:  " + entry.getTitle());
        System.out.println("| Date:   " + entry.getDate());
        System.out.println("================================");
    }

    /** 
     * Display main menu options.
    */
    public static void menuOptions() {
        System.out.println("\n\n\n~~~~~~| What do you want to do? |~~~~~~");
        System.out.println("|1| Create new diary entry");
        System.out.println("|2| See Your diaries");
        System.out.println("|3| See Diary Index");
        System.out.println("|4| See other author's diaries");
        System.out.println("|5| Logout");
        System.out.println("|6| Profile settings");
        System.out.println("|7| Exit");
        System.out.print  ("|~| ");
    }

    /** 
     * Display login menu options.
    */
    public static void loginMenu() {
        System.out.println("\n\n\n~~~~~~| Welcome to the Diary Application |~~~~~~");
        System.out.println("|1| Login");
        System.out.println("|2| Register");
        System.out.println("|3| Exit");
        System.out.print  ("|~| ");
    }

    /** 
     * Display encryption menu options.
    */
    public static void encryptionMenu() {
        System.out.println("\n\n\n~~~~~~| Encryption Options |~~~~~~");
        System.out.println("|1| Encrypt diary entries");
        System.out.println("|2| Do not encrypt diary entries");
        System.out.print  ("|~| ");
    }

    /** 
     * Display edit feature menu options.
    */
    public static void messagePromptEditFeature() {
        System.out.println("\n\n\n~~~~~~| Edit Diary Entry |~~~~~~");
        System.out.println("|1| Edit title");
        System.out.println("|2| Edit mood");
        System.out.println("|3| Edit location");
        System.out.println("|4| Edit content");
        System.out.println("|5| Back to main menu");
        System.out.print  ("|~| ");
    }

    /** 
     * Display profile settings menu options.
    */
    public static void messageProfileSettingsMenu() {
        System.out.println("\n\n\n~~~~~~| Profile Settings |~~~~~~");
        System.out.println("|1| Change email");
        System.out.println("|2| Change phone number");
        System.out.println("|3| Change description");
        System.out.println("|4| Back to main menu");
        System.out.print  ("|~| ");
    }

    public static void messagePromptSortOption() {
        System.out.println("\n\n\n~~~~~~| Sort Diary Entries |~~~~~~");
        System.out.println("|1| After Date");
        System.out.println("|2| From Date to Date");
        System.out.println("|3| None");
        System.out.println("|4| Back to main menu");
        System.out.print  ("|~| ");
    }

    //error messages
    /** *Display error message for invalid number input.*/
    public static void errorMessageNumber() {System.out.println("---| Invalid input. Please enter a valid Number. |---");}
    /** *Display error message for invalid text input.*/
    public static void errorMessageText() {System.out.println("---| Invalid input. Please enter valid text. |---");}
    /** *Display error message for no found users.*/
    public static void errorMessageNoUserFound() {System.out.println("---| No users found. |---");}
    /** *Display error message for duplicate usernames.*/
    public static void errorMessageUserExists() {System.out.println("---| User already exists. Please choose a different username. |---");}
    /** *Display error message for failed authentication.*/
    public static void errorMessageInvalidLogin() {System.out.println("---| Authentication failed: Incorrect username or password |---");}
    /** *Display error message for Non-matching passwords.*/
    public static void errorMessagePasswordsDoNotMatch() {System.out.println("---| Passwords do not match. Please try again. |---");}
    /** *Display error message for data loading issues.*/
    public static void errorMessageUnableToLoad() {System.out.println("---| Unable to load data. Please try again. |---");}
    /** *Display error message for missing console.*/
    public static void errorMessageNoConsoleFound() {System.out.println("---| No console available. Run from a terminal. |---");}
    /** *Display error message for failed user creation.*/
    public static void errorMessageFailedUserCreation() {System.out.println("---| Failed to create user. Please try again. |---");}
    /** *Display error message for encryption failure.*/
    public static void errorMessageEncryptionFailed() {System.out.println("---| Encryption error occurred. Please try again. |---");}
    /** *Display error message for authentication failure.*/
    public static void errorMessageAuthFailed() {System.out.println("---| Authentication failed. |---");}
    /** *Display error message for datawriting failure.*/
    public static void errorMessageUnableToWrite() {System.out.println("---| Unable to write data to file. Please try again. |---");}
    /** *Display error message for entry reading failiure.*/
    public static void errorMessageFailedToReadEntries() {System.out.println("---| Failed to read diary entries. |---");}
    /** *Display error message for missing diary entries.*/
    public static void errorMessageNoDiaryEntriesFound() {System.out.println("---| No diary entries found.|---");}
    /** *Display error message for console clearing failiure.*/
    public static void errorMessageUnableToClearConsole() {System.out.println("---| Could not clear console |---");}
    /** *Display error message for user creation failiure.*/
    public static void errorMessageUserCreationFailed() {System.out.println("\n\n\n---| User creation failed. Please try again. |---");}
    /** *Display error message for user creation test failiure.*/
    public static void errorMessageUserCreationTestFailed() {System.out.println("\n\n\n---| User creation test failed. |---");}
    /** *Display error message for Mood creation failiure.*/
    public static void errorMessageMoodCreationTestFailed() {System.out.println("\n\n\n---| Mood creation test failed. |---");}
    /** *Display error message for Location creation failiure.*/
    public static void errorMessageLocationCreationTestFailed() {System.out.println("\n\n\n---| Location creation test failed. |---");}
    /** *Display error message for diary entry creation failiure.*/
    public static void errorMessageEntryCreationTestFailed() {System.out.println("\n\n\n---| Diary entry creation failed. Please try again. |---");}
    /** *Display error message for user edit test failiure.*/
    public static void errorMessageUserEditTestFailed() {System.out.println("\n\n\n---| User edit test failed. |---");}
    /** *Display error message for invalid date format.*/
    public static void errorMessageInvalidDateFormat() {System.out.println("---| Invalid date format. Please use yyyy-MM-dd |---");}

    //prompts for user input
    /** * Prompt message for username input.*/
    public static void messagePromptUsername() {System.out.print("| Enter username: ");}
    /** * Prompt message for password input.*/
    public static void messagePromptPassword() {System.out.print("| Enter password: ");}
    /** * Prompt message for repeat password input.*/
    public static void messagePromptPasswordAgain() {System.out.print("| Repeat password: ");}
    /** * Prompt message for email input.*/
    public static void messagePromptemail() {System.out.print("| Enter email: ");}
    /** * Prompt message for phone number input.*/
    public static void messagePromptPhone() {System.out.print("| Enter phone number: ");}
    /** * Prompt message for description input.*/
    public static void messagePromptDescription() {System.out.print("| Enter description: ");}
    /** * Prompt message for title input.*/
    public static void messagePromptTitle() {System.out.print("| Enter title: ");}
    /** * Prompt message for mood input.*/
    public static void messagePromptMood() {System.out.print("| Enter mood: ");}
    /** * Prompt message for location input.*/
    public static void messagePromptLocation() {System.out.print("| Enter location: ");}
    /** * Prompt message for entry ID input.*/
    public static void messagePromptID() {System.out.print("| Enter diary entry ID: ");}
    /** * Prompt message for new location input.*/
    public static void messagePromptNewLocation() {System.out.print("| Enter new location name: ");}
    /** * Prompt message for choosing location input.*/
    public static void messagePromptChooseLocation() {System.out.print("| Choose location by number: ");}
    /** * Prompt message for new mood input.*/
    public static void messagePromptNewMood() {System.out.print("| Enter new mood name: ");}
    /** * Prompt message for choosing mood input.*/
    public static void messagePromptChooseMood() {System.out.print("| Choose mood by number: ");}
    /** * Prompt message for author's name input.*/
    public static void messagePromptAuthorName() {System.out.print("| Enter Author's name: ");}
    /** * Prompt message for date input.*/
    public static void messagePromptDate() {System.out.print("| Enter date (yyyy-MM-dd): ");}
    /** * Prompt message for diary text input.*/
    public static void messageDiaryEntries() {System.out.println("===== Diary Entries =====");}
    /** * Prompt message for diary text input.*/
    public static void messagePromptText() {System.out.print("|~| ");}

    /** * Confirmation message for successful user creation.*/
    public static void messageUserCreated() {System.out.println("\n\n\n===== User created successfully! =====");}
    /** * Confirmation message for successful profile update.*/
    public static void messageProfileUpdated() {System.out.println("\n\n\n===== Profile updated successfully! =====");}
    /** * Confirmation message for successful login.*/
    public static void messageLogin() {System.out.println("\n\n\n=====Login succsess=====");}
    /** * Display current logged-in user.*/
    public static void currentUser(User user) {System.out.println("===== Logged in as: " + user.getUserName() + " =====");}
    /** * Confirmation message for successful user creation test.*/
    public static void messageUserCreationTestSuccess() {System.out.println("\n\n\n===== User creation test succeeded! =====");}
    /** * Confirmation message for successful mood creation test.*/
    public static void messageMoodCreationTestSuccess() {System.out.println("\n\n\n===== Mood creation test succeeded! =====");}
    /** * Confirmation message for successful location creation test.*/
    public static void messageLocationCreationTestSuccess() {System.out.println("\n\n\n===== Location creation test succeeded! =====");}
    /** * Confirmation message for successful diary entry creation test.*/
    public static void messageEntryCreationTestSuccess() {System.out.println("\n\n\n===== Entry creation test succeeded! =====");}
    /** * Confirmation message for successful user edit test.*/
    public static void messageUserEditTestSuccess() {System.out.println("\n\n\n===== User edit test succeeded! =====");}


    //headings, or short prompts that require multiple lines
    /** * Prompt user to create or login.*/ 
    public static void messageUserCreateOrLogin() {System.out.print("\n\n\n~~~~ Choose action ~~~~\n|1| Create new user\n|2| Login\n|~| ");}
    /** * Informational message for creating a new diary entry.*/
    public static void messagecreatingEntry() {System.out.println("\n\n\n~~~~~~ Creating a new diary entry ~~~~~~");}
    /** * Prompt message for diary content input.*/
    public static void messagePromptContent() {System.out.print("~~~~| Enter diary content (type 'END' on a new line to finish) |~~~~");}
    /** * Prompt message for encryption choice.*/
    public static void messagePromptEncryptionChoice() {System.out.print("~~~~| do you want to encrypt the content? |~~~~\n|1| No\n|2| Yes\n|~| ");}
    /** * Confirmation message for successful logout.*/
    public static void messageLogout() {System.out.println("~~~~~~| Logging out... |~~~~~~");}
    /** * Confirmation message for exiting the application.*/   
    public static void messageExit() {System.out.println("~~~~~~| Exiting Diary Application. Goodbye! |~~~~~~");}
}