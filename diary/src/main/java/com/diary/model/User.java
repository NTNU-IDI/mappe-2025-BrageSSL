package com.diary.model;

import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * User model class.
 */
public class User {

    /**
     * User attributes.
     */
    private final String userId;
    private final String userName;
    private String email;
    private String phoneNumber;
    private String description;
    private final String userPassword;
    private final String encodedKey; // Base64 version of SecretKey

    /**
     * Constructor used by Jackson when reading JSON.
     */
    @JsonIgnore
    private SecretKey userKey;

    /**
     * Constructor used by Jackson when reading JSON.
     * 
     * @param userId
     * @param userName
     * @param email
     * @param phoneNumber
     * @param description
     * @param userPassword
     * @param encodedKey
     */
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

    /**
     * Retrieves the user's ID.
     * 
     * @return User's ID as a String.
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Retrieves the user's username.
     * 
     * @return User's username as a String.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Retrieves the user's email.
     * 
     * @return User's email as a String.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Retrieves the user's phone number.
     * 
     * @return User's phone number as a String.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Retrieves the user's description.
     * 
     * @return User's description as a String.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Retrieves the user's password.
     * 
     * @return User's password as a String.
     */
    public String getUserPassword() {
        return userPassword;
    }

    /**
     * Retrieves the Base64 encoded version of the user's SecretKey.
     * 
     * @return Base64 encoded key as a String.
     */
    public String getEncodedKey() {
        return encodedKey;
    }

    /**
     * Sets new email for the user.
     * 
     * @param email New email to set.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Sets new phone number for the user.
     * 
     * @param phoneNumber New phone number to set.
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Sets new description for the user.
     * 
     * @param description New description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Retrieves the SecretKey for the user, decoding it from Base64 if necessary.
     * 
     * @return SecretKey associated with the user.
     */
    @JsonIgnore
    public SecretKey getUserKey() {
        if (userKey == null && encodedKey != null) {
            byte[] decoded = Base64.getDecoder().decode(encodedKey);
            userKey = new SecretKeySpec(decoded, 0, decoded.length, "AES");
        }
        return userKey;
    }
}