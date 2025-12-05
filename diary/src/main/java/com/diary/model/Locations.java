package com.diary.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/** 
 * Location model class.
*/
public class Locations {
    /** 
     * Location attributes.
    */
    String location;
    String creator;

    /**
     * Constructor used by Jackson when reading JSON.   
     * @param location
     * @param creator
     */
    @JsonCreator
    public Locations(
            @JsonProperty("location") String location, 
            @JsonProperty("creator") String creator) 
            {
        this.location = location;
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
     * Retrieves the location.
     * @return Location as a String.
    */
    public String getLocation() {
        return location;
    }
}
