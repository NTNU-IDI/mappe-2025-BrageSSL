package com.diary.model;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Scanner;
import java.util.UUID;

import com.diary.util.EncryptionUtil;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

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
    public DiaryEntry(User user, Scanner scanner) throws Exception {
        System.out.print("Enter diary title: ");
        this.title = scanner.nextLine();

        System.out.print("Enter mood: ");
        this.mood = scanner.nextLine();

        System.out.print("Enter location: ");
        this.location = scanner.nextLine();

        System.out.print("Enter diary content: ");
        String content = scanner.nextLine();

        System.out.println("Do you wish to encrypt the content? \n1. yes: \n2. no ");
        int choice  = scanner.nextInt();
        boolean valg = true;
        boolean encrypt = true;
        while (valg) { 
            switch (choice) {
                case 1 :
                    encrypt = true;
                    valg = false;
                    break;
                case 2:
                    encrypt = false;
                    valg = false;
                    break;
                default :
                    System.out.print("Not a valid awnser try again ");
                    break;
            } 
        }
        this.encrypted =encrypt;
        byte[] encrypts = EncryptionUtil.encrypt(content, user.getUserKey());
        this.encryptedContent = encrypts;

        if (encrypt == false){
            this.encodedContent = "";
            this.publicContent = content;
        } else {  
            this.encodedContent = Base64.getEncoder().encodeToString(encrypts);
            this.publicContent = "";
        }
        
        LocalDateTime now = LocalDateTime.now();
        this.date = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), now.getHour(), now.getMinute());
        this.id = UUID.randomUUID().toString();
        this.author = user.getUserName();
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

    @JsonIgnore
    public byte[] getEncryptedContent() {
        if (encryptedContent == null && encodedContent != null) {
            return Base64.getDecoder().decode(encodedContent);
        }
        return encryptedContent;
    }
}