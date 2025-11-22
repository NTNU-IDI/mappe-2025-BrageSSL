package com.diary.model;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Moods {
    String mood;
    String creator;

    @JsonCreator
    public Moods(
            @JsonProperty("mood") String mood, 
            @JsonProperty("creator") String creator) 
            {
        this.mood = mood;
        this.creator = creator;
    }

    public String getCreator() {
        return creator;
    }
    
    public String getMood() {
        return mood;
    }
}
