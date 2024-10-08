package com.example.finalprojonline;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DodgeballTeam extends SportsTeam {

    private int players;
    private int wins;
    private int losses;
    private static int colorIndex = 0;
    private static final Color[] BASE_COLORS = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.ORANGE};
    private static List<Color> colors = new ArrayList<>();

    public DodgeballTeam() {
        super();
    }

    public DodgeballTeam(String teamName, String sport, String state, String city, int teamID, int players, int wins, int losses) {
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

    // Getters and setters
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
        return String.format("Dodgeball Team - ID: %d, Name: %s, Sport: %s, State: %s, City: %s, Members: %d, Matches Won: %d, Matches Lost: %d",
                getTeamID(), getTeamName(), getSport(), getState(), getCity(), players, wins, losses);
    }

    @Override
    public Pane getLogo() {
        Pane logoPane = new Pane();
        logoPane.setPrefSize(100, 100); // Set size for the logo pane

        // Ensure the colorIndex is within bounds
        if (colorIndex >= colors.size()) {
            colorIndex = 0; // Reset to start of the array if exceeded
            initializeColors(); // it will shuffle the colors if needed again
        }

        // Get the color for this logo
        Color squareColor = Color.WHITE; // Set the square color to white
        Color circleColor = colors.get((colorIndex + 1) % colors.size()); // Ensure we don't exceed bounds

        // Create a square
        Rectangle square = new Rectangle(100, 100);
        square.setFill(squareColor); // Set color for the square

        // Create a circle
        Circle circle = new Circle(50, circleColor); // Circle with radius 50
        Circle dot = new Circle(5, Color.BLACK); // Small circle with radius 5

        // Center the dot inside the main circle
        dot.setTranslateX(50); // Center horizontally
        dot.setTranslateY(25); // Center vertically

        // Center the circle inside the square
        circle.setTranslateX(50); // Center horizontally
        circle.setTranslateY(50); // Center vertically

        logoPane.getChildren().addAll(square, circle, dot); // Add shapes to pane

        // Move to the next color for the next logo
        colorIndex++;

        return logoPane;
    }
}