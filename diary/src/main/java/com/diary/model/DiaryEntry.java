package com.diary.model;

import java.util.Base64;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DiaryEntry {

    private final String id;
    private final String author;
    private final String title;
    private final LocalDateTime date;
    private LocalDateTime editedTime;
    private final String mood;
    private final String location;
    private String encodedContent;
    private String publicContent;
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
            @JsonProperty("editedTime") LocalDateTime editedTime,
            @JsonProperty("mood") String mood,
            @JsonProperty("location") String location,
            @JsonProperty("encodedContent") String encodedContent,
            @JsonProperty("publicContent") String publicContent,
            @JsonProperty("encrypt") boolean encrypted) {

        this.id = id;
        this.author = author;
        this.title = title;
        this.date = date;
        this.editedTime = editedTime;
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

    // Getters (Jackson uses these when writing JSON)
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

    public String getEncodedContent() { 
        return encodedContent; 
    }

    public byte[] getEncryptedContent() {
        return encryptedContent;
    }

    public String getPublicContent() { 
        return publicContent; 
    }

    public boolean getEncrypted() { 
        return encrypted; 
    }
    
    public LocalDateTime getEditedTime() {
        return editedTime;
    }

    public void setPublicContent(String publicContent) {
        this.publicContent = publicContent;
        LocalDateTime now = LocalDateTime.now();
        this.editedTime = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), now.getHour(), now.getMinute());
    }

    public void setEncodedContent(String encodedContent) {
        this.encodedContent = encodedContent;
        LocalDateTime now = LocalDateTime.now();
        this.editedTime = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), now.getHour(), now.getMinute());
    }
}