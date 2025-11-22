package com.diary.model;

import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;


public class User {

    private final String userId;
    private final String userName;
    private String email;
    private String phoneNumber;
    private String description;
    private final String userPassword;
    private final String encodedKey; // Base64 version of SecretKey

    @JsonIgnore
    private SecretKey userKey;

    // Default constructor for Jackson
    @JsonCreator
    public User(
            @JsonProperty("userId") String userId,
            @JsonProperty("userName") String userName,
            @JsonProperty("email") String email,
            @JsonProperty("phoneNumber") String phoneNumber,
            @JsonProperty("description") String description,
            @JsonProperty("userPassword") String userPassword,
            @JsonProperty("encodedKey") String encodedKey) {

        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.description = description;
        this.userPassword = userPassword;
        this.encodedKey = encodedKey;

        if (encodedKey != null) {
            byte[] decoded = Base64.getDecoder().decode(encodedKey);
            this.userKey = new SecretKeySpec(decoded, 0, decoded.length, "AES");
        } else {
            this.userKey = null;
        }
    }

    // Getters for Jackson
    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public String getDescription() {
        return description;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getEncodedKey() {
        return encodedKey;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonIgnore
    public SecretKey getUserKey() {
        if (userKey == null && encodedKey != null) {
            byte[] decoded = Base64.getDecoder().decode(encodedKey);
            userKey = new SecretKeySpec(decoded, 0, decoded.length, "AES");
        }
        return userKey;
    }
}