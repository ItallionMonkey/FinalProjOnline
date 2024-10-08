package com.example.finalprojonline;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.text.Text;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TeamManagerApp extends Application {
    private static final int MAX_TEAMS = 100;
    private final ArrayList<SportsTeam> teamList = new ArrayList<>();
    private int teamCount = 100; // Start IDs from 100

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        showMainScreen(primaryStage);
    }
//asks for 4 data types. (Name,team type, state, city)
    //after this is finished prompting the user it will take team type and apply appropriate questions found in showApexFields, showBaseBallFields , showDodgeBallFields
    private void showMainScreen(Stage primaryStage) {
        Label teamNameLabel = new Label("Enter a Team Name:");
        TextField teamNameField = new TextField();
        teamNameField.setPromptText("Team Name");

        Label teamTypeLabel = new Label("Please enter a Team type (Apex, Baseball, or Dodgeball):");
        TextField teamTypeField = new TextField();
        teamTypeField.setPromptText("Team type");

        Label stateLabel = new Label("Enter the team's State:");
        TextField stateField = new TextField();
        stateField.setPromptText("State");

        Label cityLabel = new Label("Enter the City:");
        TextField cityField = new TextField();
        cityField.setPromptText("City");

        Button submitButton = new Button("Submit");
        Button viewTeamsButton = new Button("View All Teams");
        Button manageTeamsButton = new Button("Manage Teams");
        Button searchButton = new Button("Search by ID");
        Button saveButton = new Button("Save Teams");
        Button loadButton = new Button("Load Teams");

        VBox vbox = new VBox(10, teamNameLabel, teamNameField, teamTypeLabel, teamTypeField, stateLabel, stateField, cityLabel, cityField, submitButton, viewTeamsButton, manageTeamsButton, searchButton, saveButton, loadButton);
        Scene scene = new Scene(vbox, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Create a New Team");
        primaryStage.show();

        submitButton.setOnAction(event -> {
            String teamName = teamNameField.getText().trim();
            String teamType = teamTypeField.getText().trim();
            String state = stateField.getText().trim();
            String city = cityField.getText().trim();

            if ("Apex".equalsIgnoreCase(teamType)) {
                showApexFields(primaryStage, teamName, state, city);
            } else if ("Baseball".equalsIgnoreCase(teamType)) {
                showBaseballFields(primaryStage, teamName, state, city);
            } else if ("Dodgeball".equalsIgnoreCase(teamType)) {
                showDodgeballFields(primaryStage, teamName, state, city);
            } else {
                showErrorDialog("Invalid team type entered.");
            }
        });

        viewTeamsButton.setOnAction(event -> showTeamList(primaryStage));
        manageTeamsButton.setOnAction(event -> showManageTeamsScreen(primaryStage));
        searchButton.setOnAction(event -> showSearchScreen(primaryStage));
        saveButton.setOnAction(event -> saveTeams());
        loadButton.setOnAction(event -> {
            loadTeams();
            showMainScreen(primaryStage);
        });
    }
    //presents user with questions if apex is entered. This is the primary stage after the first field is finished
    private void showApexFields(Stage primaryStage, String teamName, String state, String city) {
        VBox vbox = new VBox(10);

        Label rankLabel = new Label("Rank:");
        TextField rankField = new TextField();
        rankField.setPromptText("Enter rank");

        Label avgDamageLabel = new Label("Average Damage:");
        TextField avgDamageField = new TextField();
        avgDamageField.setPromptText("Enter average damage");

        Label winLossRatioLabel = new Label("Win/Loss Ratio:");
        TextField winLossRatioField = new TextField();
        winLossRatioField.setPromptText("Enter win/loss ratio");

        Button submitButton = new Button("Submit");
        Button backButton = new Button("Back to Main Menu");

        submitButton.setOnAction(event -> {
            try {
                String rank = rankField.getText().trim();
                int averageDamage = Integer.parseInt(avgDamageField.getText().trim());
                double winLossRatio = Double.parseDouble(winLossRatioField.getText().trim());

                if (teamList.size() < MAX_TEAMS) {
                    ApexTeam apexTeam = new ApexTeam(teamName, "Apex Sport", state, city, teamCount++, rank, averageDamage, winLossRatio);
                    teamList.add(apexTeam);
                    System.out.println("ApexTeam created: " + apexTeam);

                    Pane logoPane = apexTeam.getLogo();
                    logoPane.setPrefSize(100, 100);
                    vbox.getChildren().add(logoPane);
                } else {
                    showErrorDialog("Maximum team capacity reached.");
                }
                showMainScreen(primaryStage);
            } catch (NumberFormatException e) {
                showErrorDialog("Invalid input for ApexTeam fields.");
            }
        });

        backButton.setOnAction(event -> showMainScreen(primaryStage));

        vbox.getChildren().addAll(rankLabel, rankField, avgDamageLabel, avgDamageField, winLossRatioLabel, winLossRatioField, submitButton, backButton);
        Scene scene = new Scene(vbox, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    //presents user with questions if baseball is entered. This is the primary stage after the first field is finished
    private void showBaseballFields(Stage primaryStage, String teamName, String state, String city) {
        VBox vbox = new VBox(10);

        Label playersLabel = new Label("Players count:");
        TextField playersField = new TextField();
        playersField.setPromptText("Enter number of players");

        Label winsLabel = new Label("Wins:");
        TextField winsField = new TextField();
        winsField.setPromptText("Enter number of wins");

        Label lossesLabel = new Label("Losses:");
        TextField lossesField = new TextField();
        lossesField.setPromptText("Enter number of losses");

        Button submitButton = new Button("Submit");
        Button backButton = new Button("Back to Main Menu");

        submitButton.setOnAction(event -> {
            try {
                int players = Integer.parseInt(playersField.getText().trim());
                int wins = Integer.parseInt(winsField.getText().trim());
                int losses = Integer.parseInt(lossesField.getText().trim());

                if (teamList.size() < MAX_TEAMS) {
                    BaseBallTeam baseballTeam = new BaseBallTeam(teamName, "Baseball", state, city, teamCount++, players, wins, losses);
                    teamList.add(baseballTeam);
                    System.out.println("BaseBallTeam created: " + baseballTeam);

                    Pane logoPane = baseballTeam.getLogo();
                    logoPane.setPrefSize(100, 100);
                    vbox.getChildren().add(logoPane);
                } else {
                    showErrorDialog("Maximum team capacity reached.");
                }
                showMainScreen(primaryStage);
            } catch (NumberFormatException e) {
                showErrorDialog("Invalid input for BaseBallTeam fields.");
            }
        });

        backButton.setOnAction(event -> showMainScreen(primaryStage));

        vbox.getChildren().addAll(playersLabel, playersField, winsLabel, winsField, lossesLabel, lossesField, submitButton, backButton);
        Scene scene = new Scene(vbox, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
        //presents user with questions if dodgeball is entered. This is the primary stage after the first field is finished
    private void showDodgeballFields(Stage primaryStage, String teamName, String state, String city) {
        VBox vbox = new VBox(10);

        Label membersLabel = new Label("Number of Team Members:");
        TextField membersField = new TextField();
        membersField.setPromptText("Enter number of team members");

        Label matchesWonLabel = new Label("Matches Won:");
        TextField matchesWonField = new TextField();
        matchesWonField.setPromptText("Enter number of matches won");

        Label matchesLostLabel = new Label("Matches Lost:");
        TextField matchesLostField = new TextField();
        matchesLostField.setPromptText("Enter number of matches lost");

        Button submitButton = new Button("Submit");
        Button backButton = new Button("Back to Main Menu");

        submitButton.setOnAction(event -> {
            try {
                int teamMembers = Integer.parseInt(membersField.getText().trim());
                int matchesWon = Integer.parseInt(matchesWonField.getText().trim());
                int matchesLost = Integer.parseInt(matchesLostField.getText().trim());

                if (teamList.size() < MAX_TEAMS) {
                    DodgeballTeam dodgeballTeam = new DodgeballTeam(teamName, "Dodgeball", state, city, teamCount++, teamMembers, matchesWon, matchesLost);
                    teamList.add(dodgeballTeam);
                    System.out.println("DodgeballTeam created: " + dodgeballTeam);

                    Pane logoPane = dodgeballTeam.getLogo();
                    logoPane.setPrefSize(100, 100);
                    vbox.getChildren().add(logoPane);
                } else {
                    showErrorDialog("Maximum team capacity reached.");
                }
                showMainScreen(primaryStage);
            } catch (NumberFormatException e) {
                showErrorDialog("Invalid input for DodgeballTeam fields.");
            }
        });

        backButton.setOnAction(event -> showMainScreen(primaryStage));

        vbox.getChildren().addAll(membersLabel, membersField, matchesWonLabel, matchesWonField, matchesLostLabel, matchesLostField, submitButton, backButton);
        Scene scene = new Scene(vbox, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    //Allows the user to manage teams by adding or removing them.
    private void showManageTeamsScreen(Stage primaryStage) {
        VBox vbox = new VBox(10);

        Label manageLabel = new Label("Add/Remove Team:");

        ObservableList<CheckBox> checkBoxList = FXCollections.observableArrayList();
        for (SportsTeam team : teamList) {
            CheckBox checkBox = new CheckBox(team.getDetails());
            checkBox.setUserData(team);
            checkBoxList.add(checkBox);
        }

        ListView<CheckBox> checkBoxListView = new ListView<>(checkBoxList);

        Button deleteButton = new Button("Delete Selected Teams");
        Button backButton = new Button("Back to Main Menu");

        deleteButton.setOnAction(event -> {
            List<CheckBox> selectedCheckBoxes = checkBoxListView.getItems().stream()
                    .filter(CheckBox::isSelected)
                    .collect(Collectors.toList());

            List<SportsTeam> teamsToRemove = new ArrayList<>();
            for (CheckBox checkBox : selectedCheckBoxes) {
                SportsTeam team = (SportsTeam) checkBox.getUserData();
                teamsToRemove.add(team);
            }

            teamList.removeAll(teamsToRemove);
            showManageTeamsScreen(primaryStage);
        });

        backButton.setOnAction(event -> showMainScreen(primaryStage));

        vbox.getChildren().addAll(manageLabel, checkBoxListView, deleteButton, backButton);
        Scene scene = new Scene(vbox, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    // takes the stage object (primary stage ) as a parameter.
    //the method is responsible for displaying the appropriate list of teams
    private void showTeamList(Stage primaryStage) {
        ListView<VBox> listView = new ListView<>();
        ObservableList<VBox> items = FXCollections.observableArrayList();

        for (SportsTeam team : teamList) {//goes through team list as it sorts though sportsTeam Object
            VBox vbox = new VBox(10);
            Text details = new Text(team.getDetails());

            Pane logoPane = null;
            if (team instanceof ApexTeam) {
                logoPane = ((ApexTeam) team).getLogo();
            } else if (team instanceof BaseBallTeam) {
                logoPane = ((BaseBallTeam) team).getLogo();
            } else if (team instanceof DodgeballTeam) {
                logoPane = ((DodgeballTeam) team).getLogo();
            }

            if (logoPane != null) {
                logoPane.setPrefSize(100, 100);
                vbox.getChildren().add(logoPane);
            }
            vbox.getChildren().add(details);
            items.add(vbox);
        }

        listView.setItems(items);

        Button backButton = new Button("Back to Main Menu");
        backButton.setOnAction(event -> showMainScreen(primaryStage));

        VBox listVBox = new VBox(10, listView, backButton);
        Scene scene = new Scene(listVBox, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    // takes user input and allows the user to enter an integer to find said team.
    //showSearch Screen is loaded into the primary stage and can comunicate to look uyp past entries saved via File .io found in load teams
    private void showSearchScreen(Stage primaryStage) {
        VBox vbox = new VBox(10);

        Label searchLabel = new Label("Enter Team ID to Search:");
        TextField searchField = new TextField();
        searchField.setPromptText("Team ID");

        Button searchButton = new Button("Search");
        Button backButton = new Button("Back to Main Menu");

        searchButton.setOnAction(event -> {
            try {
                int teamID = Integer.parseInt(searchField.getText().trim());//parse sets it as integer
                //Call a method to find the team by the parsed team ID
                SportsTeam foundTeam = findTeamByID(teamID);

                if (foundTeam != null) {
                    showTeamDetails(primaryStage, foundTeam);
                } else {
                    showTeamNotFound(primaryStage);
                }
            } catch (NumberFormatException e) {
                showErrorDialog("Invalid ID format.");
            }
        });

        backButton.setOnAction(event -> showMainScreen(primaryStage));

        vbox.getChildren().addAll(searchLabel, searchField, searchButton, backButton);
        Scene scene = new Scene(vbox, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    // search for team via team id
    //starts at 100 for team type of 1, then increments by 1 for each new team added
        private SportsTeam findTeamByID(int teamID) {
        return teamList.stream()
                .filter(team -> team.getTeamID() == teamID)
                .findFirst()
                .orElse(null);
    }

    private void showTeamDetails(Stage primaryStage, SportsTeam team) {
        VBox vbox = new VBox(10);
        Text details = new Text(team.getDetails());

        Pane logoPane = team.getLogo();
        logoPane.setPrefSize(100, 100);

        vbox.getChildren().addAll(details, logoPane);

        Button backButton = new Button("Back to Search");
        backButton.setOnAction(event -> showSearchScreen(primaryStage));

        vbox.getChildren().add(backButton);
        Scene scene = new Scene(vbox, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    // showTeamNotFound is a requirement to cover user id not found
    private void showTeamNotFound(Stage primaryStage) {
        VBox vbox = new VBox(10);
        Text notFoundText = new Text("Team not found.");

        Button backButton = new Button("Back to Search");
        backButton.setOnAction(event -> showSearchScreen(primaryStage));

        vbox.getChildren().addAll(notFoundText, backButton);
        Scene scene = new Scene(vbox, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    // saves team using file .io stream to a file named teams.dat in the same directory as the program

    private void saveTeams() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("teams.dat"))) {
            oos.writeObject(teamList);
            System.out.println("Teams saved successfully.");
        } catch (IOException e) {
            showErrorDialog("Error saving teams.");
        }
    }
    // load pre existing teams via file .io stream from a file named teams.dat in the same directory as the program

    @SuppressWarnings("unchecked")
    private void loadTeams() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("teams.dat"))) {
            List<SportsTeam> loadedTeams = (List<SportsTeam>) ois.readObject();
            teamList.clear();
            teamList.addAll(loadedTeams);
            System.out.println("Teams loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            showErrorDialog("Error loading teams.");
        }
    }
    // handles different usages of dialog for the program, such as error handling and user feedback

    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}