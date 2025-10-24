package com.diary;

import java.io.File;
import java.util.List;
import java.util.Scanner;

import com.diary.manager.DiaryManager;
import com.diary.model.DiaryEntry;
import com.diary.model.User;
import com.diary.util.DiaryRead;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class Main {
    public static void main(String[] args) {
        User user = null;
        try (Scanner scanner = new Scanner(System.in)) {

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.enable(SerializationFeature.INDENT_OUTPUT);


            // ---------- Load users ----------
            File userFile = new File("diary/data/user.json");
            List<User> users = DiaryManager.loadUser(userFile, mapper);

             // ---------- Load diary entries ----------
            File diaryFile = new File("diary/data/diary.json");
            List<DiaryEntry> entries = DiaryManager.loadEntries(diaryFile, mapper);
            
            // ---------- Load locations ----------
            File locationFile = new File("diary/data/location.json");
            List<DiaryManager> location = DiaryManager.loadLocations(locationFile, mapper);
            
            // ---------- Load moods ----------
            File moodFile = new File("diary/data/mood.json");
            List<DiaryManager> mood = DiaryManager.loadMood(moodFile, mapper);
            boolean running = true;
            if (users.isEmpty()){
                System.out.println("---| No users found, creating new user |---");
                User newUser = new User(scanner);
                users.add(newUser);
                mapper.writeValue(userFile, users);
                DiaryRead.clearConsole();
                System.out.println("~~~~~~| Logging inn |~~~~~~");
                user = User.userAuth(users, scanner);              
            }
                
            else{
                while (running) {
                    DiaryRead.clearConsole();
                    System.out.println("|1| Logg inn \n|2| Register");
                    String input = scanner.nextLine().trim();
                    int choice = 0;
                    try {
                        choice = Integer.parseInt(input); // safely parse
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a number 1 or 2");
                        continue;
                    }
                    switch (choice) {
                        case 1:
                            DiaryRead.clearConsole();
                            System.out.println("~~~~~~| Logging inn |~~~~~~");
                            user = User.userAuth(users, scanner);
                            running = false;
                            break;

                        case 2:
                            DiaryRead.clearConsole();
                            System.out.println("~~~~~~| Creating new user |~~~~~~");
                            user = new User(scanner);
                            users.add(user);
                            mapper.writeValue(userFile, users);
                            DiaryRead.clearConsole();
                            System.out.println("~~~~~~| Logg inn |~~~~~~");
                            user = User.userAuth(users, scanner);
                            running = false;
                            break;
                        default:
                            DiaryRead.clearConsole();
                            System.out.println("Invalid choice. Please try again.");
                            try {
                                Thread.sleep(1000); // wait 3 seconds before throwing
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                            break;
                    }
                }
            }  

            running = true;

            DiaryRead.clearConsole();
            while (running) { 
                System.out.println("What do you want to do? \n1. create new diary entry? \n2. See Diary Index? \n3. See Your diaries? \n4. See other author's diaries? \n5. Exit");
                String input = scanner.nextLine().trim();
                int choice;
                try {
                    choice = Integer.parseInt(input); // safely parse
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number between 1 and 5.");
                    continue; // restart the loop
                }
                switch (choice) {
                    case 1:
                        DiaryRead.clearConsole();
                        DiaryEntry newEntry = new DiaryEntry(user, moodFile, locationFile, scanner, mapper);
                        entries.add(newEntry);
                        // Save diary entries
                        mapper.writeValue(diaryFile, entries);
                        System.out.println("Diary entry saved successfully!"); 
                        location = DiaryManager.loadLocations(locationFile, mapper);
                        mood = DiaryManager.loadMood(moodFile, mapper);               
                        break;
                    case 2:
                        DiaryRead.clearConsole();
                        DiaryRead.showIndex(diaryFile);
                        break;
                    case 3:
                        DiaryRead.clearConsole();
                        DiaryRead.myIndex(user, diaryFile);
                        break;
                    case 4:
                        DiaryRead.clearConsole();
                        System.out.print("Choose author:");
                        String Author = scanner.nextLine();
                        DiaryRead.otherIndex(Author, diaryFile);
                        break;
                    case 5:
                        DiaryRead.clearConsole();
                        running = false;
                        break;
                    default:
                        DiaryRead.clearConsole();
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}