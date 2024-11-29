package com.example.finalprojonline;

public class ALGSManager extends ApexTeam {

    private int gameTag;
    private int age;
    private String residency;
    private boolean isSignedContract;

    public ALGSManager() {
        super();
    }


    public ALGSManager(String teamName, String sport, String state, String city, int teamID, String rank, int averageDamage, double winLossRatio,
                      int gameTag, int age, String residency, boolean isSignedContract) {
        super(teamName, sport, state, city, teamID, rank, averageDamage, winLossRatio);
        this.gameTag = gameTag;
        this.age = age;
        this.residency = residency;
        this.isSignedContract = isSignedContract;
    }

    // Getters and Setters for AlgsManger-specific fields
    public int getGameTag() {
        return gameTag;
    }

    public void setGameTag(int gameTag) {
        this.gameTag = gameTag;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getResidency() {
        return residency;
    }

    public void setResidency(String residency) {
        this.residency = residency;
    }

    public boolean isSignedContract() {
        return isSignedContract;
    }

    public void setSignedContract(boolean signedContract) {
        isSignedContract = signedContract;
    }

    @Override
    public String getDetails() {
        return String.format("Algs Manager - ID: %d, Name: %s, Sport: %s, State: %s, City: %s, Rank: %s, Avg Damage: %d, Win/Loss Ratio: %.2f, GameTag: %d, Age: %d, Residency: %d, Signed Contract: %b",
                getTeamID(), getTeamName(), getSport(), getState(), getCity(), getRank(), getAverageDamage(), getWinLossRatio(), gameTag, age, residency, isSignedContract);
    }
}
