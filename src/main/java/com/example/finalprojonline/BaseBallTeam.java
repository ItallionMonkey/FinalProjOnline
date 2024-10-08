package com.example.finalprojonline;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Ellipse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BaseBallTeam extends SportsTeam {
    private int players;
    private int wins;
    private int losses;

    // Color counter to cycle through colors
    private static int colorIndex = 0;
    private static final Color[] BASE_COLORS = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.ORANGE};
    private static List<Color> colors = new ArrayList<>();

    public BaseBallTeam() {
        super();
        initializeColors();
    }

    public BaseBallTeam(String teamName, String sport, String state, String city, int teamID, int players, int wins, int losses) {
        super(teamName, sport, state, city, teamID);
        this.players = players;
        this.wins = wins;
        this.losses = losses;
        initializeColors();
    }

    private void initializeColors() {
        colors.clear();
        Collections.addAll(colors, BASE_COLORS);
        Collections.shuffle(colors);
    }

    // Getters and Setters
    public int getPlayers() {
        return players;
    }

    public void setPlayers(int players) {
        this.players = players;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    @Override
    public String getDetails() {
        return String.format("Baseball Team - ID: %d, Name: %s, Sport: %s, State: %s, City: %s, Players: %d, Wins: %d, Losses: %d",
                getTeamID(), getTeamName(), getSport(), getState(), getCity(), players, wins, losses);
    }

    @Override
    public Pane getLogo() {
        Pane logoPane = new Pane();
        logoPane.setPrefSize(100, 100);
        // Ensure the colorIndex is within bounds
        if (colorIndex >= colors.size()) {
            colorIndex = 0; //
            initializeColors();
        }

        // Get the color for this logo
        Color squareColor = colors.get(colorIndex);
        Color borderColor = colors.get((colorIndex + 1) % colors.size()); // Ensure we don't exceed bounds

        // Create a square
        Rectangle square = new Rectangle(100, 100);
        square.setFill(squareColor); // Set color for the square
        square.setStroke(borderColor); // Set border color

        // Create an elongated ellipse
        Ellipse ellipse = new Ellipse(10, 40); // Ellipse with radius 10 (horizontal) and 40 (vertical)
        ellipse.setFill(borderColor); // Set color for the ellipse
        ellipse.setTranslateX(50); // Center horizontally in the pane
        ellipse.setTranslateY(50); // Center vertically in the pane

        // Add shapes to the pane
        logoPane.getChildren().addAll(square, ellipse);

        // Move to the next color for the next logo
        colorIndex++;

        return logoPane;
    }
}