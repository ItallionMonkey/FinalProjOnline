package com.example.finalprojonline;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ApexTeam extends SportsTeam {
    private String rank;
    private int averageDamage;
    private double winLossRatio;

    // Color counter to cycle through colors
    private static int colorIndex = 0;
    private static final Color[] BASE_COLORS = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.ORANGE};
    private static List<Color> colors = new ArrayList<>();

    public ApexTeam() {
        super();
        initializeColors();
    }

    public ApexTeam(String teamName, String sport, String state, String city, int teamID, String rank, int averageDamage, double winLossRatio) {
        super(teamName, sport, state, city, teamID);
        this.rank = rank;
        this.averageDamage = averageDamage;
        this.winLossRatio = winLossRatio;
        initializeColors();
    }

    private void initializeColors() {
        colors.clear();
        Collections.addAll(colors, BASE_COLORS);
        Collections.shuffle(colors);
    }

    // Getters and Setters
    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public int getAverageDamage() {
        return averageDamage;
    }

    public void setAverageDamage(int averageDamage) {
        this.averageDamage = averageDamage;
    }

    public double getWinLossRatio() {
        return winLossRatio;
    }

    public void setWinLossRatio(double winLossRatio) {
        this.winLossRatio = winLossRatio;
    }

    @Override
    public String getDetails() {
        return String.format("Apex Team - ID: %d, Name: %s, Sport: %s, State: %s, City: %s, Rank: %s, Avg Damage: %d, Win/Loss Ratio: %.2f",
                getTeamID(), getTeamName(), getSport(), getState(), getCity(), rank, averageDamage, winLossRatio);// need to add a way to save and possibly add info from ALGS
    }

    @Override
    public Pane getLogo() {
        Pane logoPane = new Pane();
        logoPane.setPrefSize(100, 100);


        if (colorIndex >= colors.size()) {
            colorIndex = 0;
            initializeColors();
        }

        // Get the color for this logo
        Color squareColor = colors.get(colorIndex);
        Color circleColor = colors.get((colorIndex + 1) % colors.size()); // Ensure we don't exceed bounds

        // Create a square
        Rectangle square = new Rectangle(100, 100);
        square.setFill(squareColor); // Set color for the square

        // Create a circle
        Circle circle = new Circle(50, circleColor); // Circle with radius 50

        // Center the circle inside the square
        circle.setTranslateX(50); // Center horizontally
        circle.setTranslateY(50); // Center vertically

        logoPane.getChildren().addAll(square, circle); // Add shapes to pane

        // Move to the next color for the next logo
        colorIndex++;

        return logoPane;
    }
}