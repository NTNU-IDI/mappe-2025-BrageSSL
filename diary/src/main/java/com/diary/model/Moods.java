package com.diary.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/** 
 * Mood model class.
*/
public class Moods {
    /** 
     * Mood attributes.
    */
    String mood;
    String creator;

    /**
     * Constructor used by Jackson when reading JSON.   
     * @param mood
     * @param creator
     */
    @JsonCreator
    public Moods(
            @JsonProperty("mood") String mood, 
            @JsonProperty("creator") String creator) 
            {
        this.mood = mood;
        this.creator = creator;
    }

    /** 
     * Retrieves the creator.
     * @return Creator as a String.
    */
    public String getCreator() {
        return creator;
    }
    
    /** 
     * Retrieves the mood.
     * @return Mood as a String.
    */
    public String getMood() {
        return mood;
    }
}
