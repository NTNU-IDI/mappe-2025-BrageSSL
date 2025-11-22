package com.diary.model;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Locations {
    String location;
    String creator;

    @JsonCreator
    public Locations(
            @JsonProperty("location") String location, 
            @JsonProperty("creator") String creator) 
            {
        this.location = location;
        this.creator = creator;
    }

    public String getCreator() {
        return creator;
    }

    public String getLocation() {
        return location;
    }
}
