package com.diary.manager;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.diary.model.DiaryEntry;
import com.diary.model.Locations;
import com.diary.model.Moods;
import com.diary.util.EncryptionUtil;
import com.diary.util.Interfaces;
import com.diary.model.User;

public class EntryManager {
    private ArrayList<String> entries;
    private ArrayList<String> moods;
    private ArrayList<String> locations;
    
    public void createEntry(User user) {
        Scanner scanner = new Scanner(System.in);

        String id = UUID.randomUUID().toString();
        String author = user.getUserName();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime date = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), now.getHour(), now.getMinute());
        LocalDateTime editedTime = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), now.getHour(), now.getMinute());

        Interfaces.messagePromptTitle();
        String title = scanner.nextLine();

        Interfaces.messagePromptContent();
        StringBuilder contentBuilder = new StringBuilder();
        String line;

        while (true) {
            line = scanner.nextLine();
            if (line.equalsIgnoreCase("END")) {
                break;
            }
            contentBuilder.append(line).append(System.lineSeparator());
        }

        String content = contentBuilder.toString().trim();

        
        boolean valg = true;
        boolean encrypt = true;
        while (valg) { 

            Interfaces.encryptionMenu();
            String input = scanner.nextLine().trim();
            int choice;
            try {
                choice = Integer.parseInt(input); // safely parse
            } catch (NumberFormatException e) {
                System.out.println("---| Invalid input. Please enter a number between 1 and  2. |---");
                continue; // restart the loop
            }
            switch (choice) {
                case 1 :
                    encrypt = true;
                    valg = false;
                    break;
                case 2:
                    encrypt = false;
                    valg = false;
                    break;
            } 
        }
        boolean encrypted = encrypt;
        byte[] encrypts = EncryptionUtil.encrypt(content, user.getUserKey());
        byte[] encryptedContent = encrypts;

        if (encrypt){
            String encodedContent = Base64.getEncoder().encodeToString(encrypts);
            String publicContent = "";
        } else {  
            String encodedContent = "";
            String publicContent = content;
        }
        
        Interfaces.showEntry(this.id, List.of(this), user);

        DiaryEntry newEntry = new DiaryEntry(id, author, title, date, editedTime, mood, location, encodedContent, publicContent, encrypted);
        entries.add(newEntry);
    }
    
    public ArrayList<Moods> findMoods(User user) {
        ArrayList<Moods> Moods = new ArrayList<>();
        ArrayList<Moods> userMoods = DiaryManager.loadMood();
        for (Moods m : moods) {
            if (m.getCreator().equals(user.getUserName())) {
                userMoods.add(m.getMood());
            }
        }
        return userMoods;
    }

    public ArrayList<Locations> findLocations(User user, ObjectMapper mapper) {
        ArrayList<String> Locations = DiaryManager.loadLocation();
        for (Locations l : locations) {
            if (l.getCreator().equals(user.getUserName())) {
                userLocations.add(l.getLocation());
            }
        }
        return userLocations;
    }

    public ArrayList<String> getMoods() {
        return moods;
    }

    public ArrayList<String> getLocations() {
        return locations;
    }
}
