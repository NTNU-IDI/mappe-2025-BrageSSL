package com.diary.model;

import com.diary.util.EncryptionUtil;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Scanner;
import java.util.UUID;

public class DiaryEntry {

    private final String id;
    private final String author;
    private final String title;
    private final LocalDateTime date;
    private final String mood;
    private final String location;

    // This will be saved in JSON
    private final String encodedContent;

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
            @JsonProperty("encodedContent") String encodedContent) {

        this.id = id;
        this.author = author;
        this.title = title;
        this.date = date;
        this.mood = mood;
        this.location = location;
        this.encodedContent = encodedContent;

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

        // Encrypt and encode
        byte[] encrypted = EncryptionUtil.encrypt(content, user.getUserKey());
        this.encryptedContent = encrypted;
        this.encodedContent = Base64.getEncoder().encodeToString(encrypted);

        this.date = LocalDateTime.now();
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

    @JsonIgnore
    public byte[] getEncryptedContent() {
        if (encryptedContent == null && encodedContent != null) {
            return Base64.getDecoder().decode(encodedContent);
        }
        return encryptedContent;
    }
}