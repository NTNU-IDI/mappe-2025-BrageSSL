package com.diary.model;

import java.util.Base64;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import java.time.LocalDateTime;
import java.io.File;

import com.diary.manager.DiaryManager;
import com.diary.util.EncryptionUtil;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DiaryEntry {

    private final String id;
    private final String author;
    private final String title;
    private final LocalDateTime date;
    private final String mood;
    private final String location;
    private final String encodedContent;
    private final String publicContent;
    private final boolean encrypted;

    // This will not be saved in JSON
    @JsonIgnore
    private final byte[] encryptedContent;

    // Constructor used by Jackson when reading JSON
    @JsonCreator
    public DiaryEntry(
            @JsonProperty("id") String id,
            @JsonProperty("author") String author,
            @JsonProperty("title") String title,
            @JsonProperty("date") LocalDateTime date,
            @JsonProperty("mood") String mood,
            @JsonProperty("location") String location,
            @JsonProperty("encodedContent") String encodedContent,
            @JsonProperty("publicContent") String publicContent,
            @JsonProperty("encrypt") boolean encrypted) {

        this.id = id;
        this.author = author;
        this.title = title;
        this.date = date;
        this.mood = mood;
        this.location = location;
        this.encodedContent = encodedContent;
        this.publicContent = publicContent;
        this.encrypted = encrypted;

        if (encodedContent != null) {
            this.encryptedContent = Base64.getDecoder().decode(encodedContent);
        } else {
            this.encryptedContent = null;
        }
    }

    // Constructor used to create a new diary entry interactively
    public DiaryEntry(User user, File moodFile, File locationFile, Scanner scanner, ObjectMapper mapper) throws Exception {

        System.out.print("| Enter diary title: ");
        this.title = scanner.nextLine();
        
        // Choose mood
        List<DiaryManager> moods = DiaryManager.chooseMood(scanner, user.getUserName(),moodFile, mapper);
        this.mood = moods.get(0).getMood();
        // Choose location
        List<DiaryManager> locations = DiaryManager.chooseLocation(scanner, user.getUserName(),locationFile, mapper);
        this.location = locations.get(0).getLocation();

        System.out.println("~~~~| Enter diary content (type 'END' on a new line to finish) |~~~~");
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

            System.out.println("~~~~| Do you wish to encrypt the content? |~~~~ \n|1| yes: \n|2| no ");
            String input = scanner.nextLine().trim();
            int choice;
            try {
                choice = Integer.parseInt(input); // safely parse
            } catch (NumberFormatException e) {
                System.out.println("---| Invalid input. Please enter a number between 1 and 5. |---");
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
        this.encrypted =encrypt;
        byte[] encrypts = EncryptionUtil.encrypt(content, user.getUserKey());
        this.encryptedContent = encrypts;

        if (encrypt){
            this.encodedContent = Base64.getEncoder().encodeToString(encrypts);
            this.publicContent = "";
        } else {  
            this.encodedContent = "";
            this.publicContent = content;
        }
        
        LocalDateTime now = LocalDateTime.now();
        this.date = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), now.getHour(), now.getMinute());
        this.id = UUID.randomUUID().toString();
        this.author = user.getUserName();
        DiaryManager.showEntry(this.id, List.of(this), user);
    }
    
    @JsonIgnore
    public byte[] getEncryptedContent() {
        if (encryptedContent == null && encodedContent != null) {
            return Base64.getDecoder().decode(encodedContent);
        }
        return encryptedContent;
    }

    // Getters (Jackson uses these when writing JSON)
    public String getId() { return id; }
    public String getAuthor() { return author; }
    public String getTitle() { return title; }
    public LocalDateTime getDate() { return date; }
    public String getMood() { return mood; }
    public String getLocation() { return location; }
    public String getEncodedContent() { return encodedContent; }
    public String getPublicContent() { return publicContent; }
    public boolean getEncrypted() { return encrypted; }
}