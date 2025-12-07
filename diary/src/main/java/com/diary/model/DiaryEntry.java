package com.diary.model;

import java.util.Base64;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DiaryEntry {
    
    /** 
     * DiaryEntry attributes.
    */
    private final String id;
    private final String author;
    private String title;
    private final LocalDateTime date;
    private LocalDateTime dateEdited;
    private String mood;
    private String location;
    private String encodedContent;
    private String publicContent;
    private boolean encrypted;

    /** 
     * Decoded encrypted content.
    */
    @JsonIgnore
    private final byte[] encryptedContent;

    /**
     * Constructor used by Jackson when reading JSON.   
     * @param id
     * @param author
     * @param title
     * @param date
     * @param dateEdited
     * @param mood
     * @param location
     * @param encodedContent
     * @param publicContent
     * @param encrypted
     */
    @JsonCreator
    public DiaryEntry(
            @JsonProperty("id") String id,
            @JsonProperty("author") String author,
            @JsonProperty("title") String title,
            @JsonProperty("date") LocalDateTime date,
            @JsonProperty("now") LocalDateTime dateEdited,
            @JsonProperty("mood") String mood,
            @JsonProperty("location") String location,
            @JsonProperty("encodedContent") String encodedContent,
            @JsonProperty("publicContent") String publicContent,
            @JsonProperty("encrypt") boolean encrypted) {

        this.id = id;
        this.author = author;
        this.title = title;
        this.date = date;
        this.dateEdited = dateEdited;
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

    /** 
     * Getters for DiaryEntry attributes.
    */
    public String getId() {
        return id;
    }

    /** 
     * Retrieves the author.
     * @return Author as a String.
    */
    public String getAuthor() {
        return author;
    }

    /** 
     * Retrieves the title.
     * @return Title as a String.
    */
    public String getTitle() {
        return title;
    }

    /** 
     * Retrieves the date.
     * @return Date as a LocalDate.
    */
    public LocalDateTime getDate() {
        return date;
    }

    /** 
     * Retrieves the mood.
     * @return Mood as a String.
    */
    public String getMood() {
        return mood;
    }

    /** 
     * Retrieves the location.
     * @return Location as a String.
    */
    public String getLocation() {
        return location;
    }

    /** 
     * Retrieves the encoded content.
     * @return Encoded content as a String.
    */
    public String getEncodedContent() {
        return encodedContent;
    }

    /** 
     * Retrieves the encrypted content.
     * @return Encrypted content as a byte array.
    */
    public byte[] getEncryptedContent() {
        return encryptedContent;
    }

    /** 
     * Retrieves the public content.
     * @return Public content as a String.
    */
    public String getPublicContent() {
        return publicContent;
    }

    /** 
     * Retrieves the encrypted status.
     * @return Encrypted status as a boolean.
    */
    public boolean getEncrypted() {
        return encrypted;
    }

    public LocalDateTime getDateEdited() {
        return dateEdited;
    }

    public void setDateEdited(LocalDateTime dateEdited) {
        this.dateEdited = dateEdited;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setPublicContent(String publicContent) {
        this.publicContent = publicContent;
    }

    public void setEncodedContent(String encodedContent) {
        this.encodedContent = encodedContent;
    }

    public void setEncrypted(boolean encrypted) {
        this.encrypted = encrypted;
    }


}