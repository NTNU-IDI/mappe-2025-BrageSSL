package com.diary;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.diary.model.DiaryEntry;
import com.diary.model.User;
import com.diary.util.DiaryRead;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.enable(SerializationFeature.INDENT_OUTPUT);

            // ---------- Load user ----------
            User user = null;
            File userFile = new File("diary/data/user.json");
            List<User> users = new ArrayList<>();
            if (userFile.exists() && userFile.length() > 0) {
                User[] loadedUsers = mapper.readValue(userFile, User[].class);
                for (User u : loadedUsers) users.add(u);
            }
            if (users.isEmpty()){
                System.out.println("--- No users found, creating new user ---");
                user = new User(true, scanner);
                users.add(user);
                mapper.writeValue(userFile, users);
                System.out.println("----Logging inn----");
                user = User.userAuth(users, scanner);              
            }
            else{
                System.out.println("1. Logg inn\n2. Register");
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume leftover newline
                switch (choice) {
                    case 1:
                        System.out.println("----Logging inn----");
                        user = User.userAuth(users, scanner);
                        break;
                    case 2:
                        System.out.println("----Creating new user----");
                        user = new User(true, scanner);
                        users.add(user);
                        mapper.writeValue(userFile, users);
                        System.out.println("----Logg inn----");
                        user = User.userAuth(users, scanner);
                        break;
                }
            }  

            // ---------- Load diary entries ----------
            File diaryFile = new File("diary/data/diary.json");
            List<DiaryEntry> entries = new ArrayList<>();
            if (diaryFile.exists() && diaryFile.length() > 0) {
                DiaryEntry[] loaded = mapper.readValue(diaryFile, DiaryEntry[].class);
                for (DiaryEntry e : loaded) entries.add(e);
            }
            boolean running = true;
            while (running) { 
                System.out.println("What do you want to do? \n1. create new diary entry? \n2. See Diary Index? \n3. See Your diaries? \n4. See other author's diaries? \n5. Exit");
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume leftover newline
                switch (choice) {
                    case 1:
                        DiaryEntry newEntry = new DiaryEntry(user, scanner);
                        entries.add(newEntry);
                        // Save diary entries
                        mapper.writeValue(diaryFile, entries);
                        System.out.println("Diary entry saved successfully!");                
                        break;
                    case 2:
                        DiaryRead.showIndex(diaryFile);
                        break;
                    case 3:
                        DiaryRead.myIndex(user, diaryFile);
                        break;
                    case 4:
                        System.out.print("Choose author:");
                        String Author = scanner.nextLine();
                        DiaryRead.otherIndex(Author, diaryFile);
                        break;
                    case 5:
                        running = false;
                        break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
