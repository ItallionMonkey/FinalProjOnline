package com.example.finalprojonline;

import javafx.scene.layout.Pane;
import java.io.Serializable;

public abstract class SportsTeam implements Serializable {
    private static final long serialVersionUID = 1L;
    private String teamName;
    private String sport;
    private String state;
    private String city;
    private int teamID;

    // Default constructor
    public SportsTeam() {
        // Default constructor
    }

    // Constructor with all fields
    public SportsTeam(String teamName, String sport, String state, String city, int teamID) {
        this.teamName = teamName;
        this.sport = sport;
        this.state = state;
        this.city = city;
        this.teamID = teamID;
    }

    // Getters and Setters
    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getTeamID() {
        return teamID;
    }

    public void setTeamID(int teamID) {
        this.teamID = teamID;
    }

    public abstract Pane getLogo();
    public abstract String getDetails();
}