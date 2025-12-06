package com.diary.model;

import java.util.Base64;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DiaryEntry {
    
    /** 
     * DiaryEntry attributes.
    */
    private final String id;
    private final String author;
    private final String title;
    private final LocalDate date;
    private final String mood;
    private final String location;
    private final String encodedContent;
    private final String publicContent;
    private final boolean encrypted;

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
            @JsonProperty("date") LocalDate date,
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
    public LocalDate getDate() {
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
}