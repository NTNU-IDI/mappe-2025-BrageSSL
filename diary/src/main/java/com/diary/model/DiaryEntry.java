package com.diary.model;
import com.diary.util.EncryptionUtil;

import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.Base64;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DiaryEntry {

    //creates all variables needed for a diary entry
    private final String id;
    private final String author;
    private final String title;
    private final LocalDateTime date;
    private final String mood;
    private final String location;
    private final String encodedContent;

    @JsonIgnore
    private final byte[] encryptedContent;

    //method to create a diary entry
    @JsonCreator
    public DiaryEntry(
            @JsonProperty("id") String id,
            @JsonProperty("author") String author,
            @JsonProperty("title") String title,
            @JsonProperty("date") LocalDateTime date,
            @JsonProperty("mood") String mood,
            @JsonProperty("location") String location,
            @JsonProperty("encodedContent") String encodedContent) {

        this.id = id;
        this.author = author;
        this.title = title;
        this.date = date;
        this.mood = mood;
        this.location = location;
        this.encodedContent = encodedContent;

          // Decode the base64 string to initialize the final field
        if (encodedContent != null) {
            this.encryptedContent = Base64.getDecoder().decode(encodedContent);
        } else {
            this.encryptedContent = null;
        }
    }

    public DiaryEntry(User user) throws Exception {
        Scanner scanner = new Scanner(System.in);

        //gathers all information needed for a diary entry
        System.out.print("Enter diary title: ");
        this.title = scanner.nextLine();

        //gathers mood
        System.out.print("Enter mood: ");
        this.mood = scanner.nextLine();

        //gathers location
        System.out.print("Enter current location: ");
        this.location = scanner.nextLine();

        //gathers diary content
        System.out.print("Enter diary content: ");
        String content = scanner.nextLine();

        //encrypts the diary content using the user's key
        this.encryptedContent = EncryptionUtil.encrypt(content, user.getUserKey());
        this.encodedContent = Base64.getEncoder().encodeToString(encryptedContent);

        //sets date, id, and author
        this.date = LocalDateTime.now();
        this.id = UUID.randomUUID().toString();
        this.author = user.getUserName();
    }

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getMood() {
        return mood;
    }

    public String getLocation() {
        return location;
    }

    public byte[] getEncryptedContent() {
        if (encryptedContent == null && encodedContent != null) {
            return Base64.getDecoder().decode(encodedContent);
        }
        return encryptedContent;
    }

}
