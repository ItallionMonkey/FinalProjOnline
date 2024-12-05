package com.example.finalprojonline;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.text.Text;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TeamManagerApp extends Application {
    private static final int MAX_TEAMS = 100;
    private final ArrayList<SportsTeam> teamList = new ArrayList<>();
    private final BinarySearchTree apexTeamTree = new BinarySearchTree(); // Initialize BinarySearchTree
    private int teamCount = 100; // Start IDs from 100

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        showMainScreen(primaryStage);
    }

    private void showMainScreen(Stage primaryStage) {
        primaryStage.setTitle("Team Manager");
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

        // MenuBar with options
        MenuBar menuBar = new MenuBar();
        Menu menuOptions = new Menu("Options");
        MenuItem viewTeamsItem = new MenuItem("View All Teams");
        MenuItem manageTeamsItem = new MenuItem("Manage Teams");
        MenuItem searchByIdItem = new MenuItem("Search by ID");
        MenuItem saveTeamsItem = new MenuItem("Save Teams");
        MenuItem loadTeamsItem = new MenuItem("Load Teams");
        MenuItem checkHighWinLossItem = new MenuItem("Check High Win/Loss Ratio");
        saveTeamsItem.setOnAction(event -> saveTeams());
        loadTeamsItem.setOnAction(event -> loadTeams());

        menuOptions.getItems().addAll(viewTeamsItem, manageTeamsItem, searchByIdItem, checkHighWinLossItem ,saveTeamsItem, loadTeamsItem );
        menuBar.getMenus().add(menuOptions);

        VBox vbox = new VBox(10, menuBar, teamNameLabel, teamNameField, teamTypeLabel, teamTypeField, stateLabel, stateField, cityLabel, cityField, submitButton);
        Scene scene = new Scene(vbox, 800, 800);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Action for Submit Button
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

        // Menu Item Actions
        viewTeamsItem.setOnAction(event -> showTeamList(primaryStage));
        manageTeamsItem.setOnAction(event -> showManageTeamsScreen(primaryStage));
        searchByIdItem.setOnAction(event -> showSearchScreen(primaryStage));
        checkHighWinLossItem.setOnAction(event -> {
            ApexTeam highestKDTeam = apexTeamTree.findHighestWinLoss();
            if (highestKDTeam != null) {
                showInfoDialog("Highest Apex W/L", highestKDTeam.getDetails());
            } else {
                showInfoDialog("Highest Apex W/L", "No Apex teams available.");
            }
        });
    }


    private void showApexFields(Stage primaryStage, String teamName, String state, String city) {
        VBox vbox = new VBox(10);
        Map<String, String> tempTeamData = new HashMap<>();

        Label rankLabel = new Label("Rank:");
        TextField rankField = new TextField();
        rankField.setPromptText("Enter rank");

        Label avgDamageLabel = new Label("Average Damage:");
        TextField avgDamageField = new TextField();
        avgDamageField.setPromptText("Enter average damage");

        Label winLossRatioLabel = new Label("Win/Loss Ratio:");
        TextField winLossRatioField = new TextField();
        winLossRatioField.setPromptText("Enter win/loss ratio");
        if (teamList.size() >= MAX_TEAMS) {
            showErrorDialog("Maximum team capacity reached.");
            return;
        }
        Button saveCurrentInfo = new Button("Save");
        Button submitButton = new Button("Submit Apex Team");
        Button createAlgsManagerButton = new Button("Algs Manager"); // New button
        Button backButton = new Button("Back to Main Menu");
        saveCurrentInfo.setOnAction(event -> {
            try {
                // Validate inputs before saving
                if (rankField.getText().trim().isEmpty() ||
                        avgDamageField.getText().trim().isEmpty() ||
                        winLossRatioField.getText().trim().isEmpty()) {
                    showErrorDialog("Please fill all fields before saving.");
                    return;
                }

                // Validate numeric fields
                try {
                    Integer.parseInt(avgDamageField.getText().trim());
                    Double.parseDouble(winLossRatioField.getText().trim());
                } catch (NumberFormatException e) {
                    showErrorDialog("Please enter valid numbers for Average Damage and Win/Loss Ratio.");
                    return;
                }

                // Save the current information
                tempTeamData.put("rank", rankField.getText().trim());
                tempTeamData.put("averageDamage", avgDamageField.getText().trim());
                tempTeamData.put("winLossRatio", winLossRatioField.getText().trim());

                showInfoDialog("Save Successful", "Team information has been saved.");

                // Enable the Algs Manager button after saving
                createAlgsManagerButton.setDisable(false);
            } catch (Exception e) {
                showErrorDialog("Error saving data: " + e.getMessage());
            }
        });

        // Initially disable Algs Manager button until data is saved
        createAlgsManagerButton.setDisable(true);

        // Handle Apex Team submission
        submitButton.setOnAction(event -> {
            try {
                String rank = rankField.getText().trim();
                int averageDamage = Integer.parseInt(avgDamageField.getText().trim());
                double winLossRatio = Double.parseDouble(winLossRatioField.getText().trim());

                if (teamList.size() < MAX_TEAMS) {
                    ApexTeam apexTeam = new ApexTeam(teamName, "Apex Sport", state, city, teamCount++, rank, averageDamage, winLossRatio);
                    teamList.add(apexTeam);
                    apexTeamTree.insert(apexTeam); // Insert into binary search tree
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

        // Handle AlgsManger creation
        createAlgsManagerButton.setOnAction(event -> {
            if (tempTeamData.isEmpty()) {
                showErrorDialog("Please save team information first.");
                return;
            }
            showALGSManagerFields(
                    primaryStage,
                    teamName,
                    state,
                    city,
                    rankField.getText(),
                    avgDamageField.getText(),
                    winLossRatioField.getText()
            );
        });
        backButton.setOnAction(event -> showMainScreen(primaryStage));

        vbox.getChildren().addAll(rankLabel, rankField, avgDamageLabel, avgDamageField, winLossRatioLabel, winLossRatioField, submitButton, saveCurrentInfo, createAlgsManagerButton, backButton);
        Scene scene = new Scene(vbox, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showALGSManagerFields(Stage primaryStage, String teamName, String state, String city, String rank, String avgDamage, String winLossRatio) {
        VBox vbox = new VBox(10);

        // Input fields for AlgsManger-specific data
        Label gameTagLabel = new Label("Game Tag:");
        TextField gameTagField = new TextField();
        gameTagField.setPromptText("Enter game tag");

        Label ageLabel = new Label("Age:");
        TextField ageField = new TextField();
        ageField.setPromptText("Enter age");

        Label residencyLabel = new Label("Residency:");
        TextField residencyField = new TextField();
        residencyField.setPromptText("Enter residency");

        CheckBox contractCheckbox = new CheckBox("Signed Contract");

        // Terms and Service PDF viewing button
        Label termsLabel = new Label("View Terms and Service:");
        Button viewTermsButton = new Button("View Terms and Service");
        viewTermsButton.setOnAction(event -> {
            File termsFile = new File("\"C:\\Users\\VANGURAAL23\\Downloads\\algs-2-0-rules-20220513.pdf\"");
            if (termsFile.exists()) {
                openPdfFile(String.valueOf(termsFile));
            } else {
                showErrorDialog("Terms and Service PDF not found. Please ensure it is available.");
            }
        });

        // Submit button
        Button submitButton = new Button("Submit Algs Manager");
        submitButton.setOnAction(event -> {
            try {
                // Parse inputs and validate
                String gameTag = (gameTagField.getText().trim());
                int age = Integer.parseInt(ageField.getText().trim());
                String residency = residencyField.getText().trim();
                boolean isSignedContract = contractCheckbox.isSelected();

                // Validate age
                if (age < 16) {
                    throw new IllegalArgumentException("Age must be 16 or older.");
                }

                // Validate residency
                if (residency.isEmpty()) {
                    throw new IllegalArgumentException("Residency must be filled in.");
                }
                // Parse avgDamage and winLossRatio safely
                int averageDamage;
                double winLoss;
                try {
                    averageDamage = Integer.parseInt(avgDamage);
                    winLoss = Double.parseDouble(winLossRatio);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid numeric input for Average Damage or Win/Loss Ratio.");
                }

                // Add AlgsManger if inputs are valid
                if (teamList.size() <= 20) {
                    ALGSManager algsManager = new ALGSManager(teamName, "Apex Sport", state, city, teamCount++, rank, averageDamage, winLoss, gameTag, age, residency, isSignedContract);
                    teamList.add(algsManager);
                    apexTeamTree.insert(algsManager); // Insert into binary search tree
                    System.out.println("AlgsManger created: " + algsManager);

                    Pane logoPane = algsManager.getLogo();
                    logoPane.setPrefSize(800, 800);
                    vbox.getChildren().add(logoPane);
                }

                else {
                    showErrorDialog("Maximum team capacity reached.");
                }
                showMainScreen(primaryStage);
            } catch (NumberFormatException e) {
                showErrorDialog("Invalid input. Please ensure all fields are filled out correctly.");
            } catch (IllegalArgumentException e) {
                showErrorDialog(e.getMessage());
            }
        });

        // Back button
        Button backButton = new Button("Back");
        backButton.setOnAction(event -> showApexFields(primaryStage, teamName, state, city));

        // Add all elements to the VBox
        vbox.getChildren().addAll(
                gameTagLabel, gameTagField,
                ageLabel, ageField,
                residencyLabel, residencyField,
                contractCheckbox,
                termsLabel, viewTermsButton,
                submitButton, backButton
        );

        // Set up the scene
        Scene scene = new Scene(vbox, 800, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    private void openPdfFile(String pdfFileName) {
        try {
            // Get the resource as a stream from the project
            InputStream inputStream = getClass().getResourceAsStream("algs-2-0-rules-20220513.pdf" + pdfFileName);


        }
    }





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

    private void showTeamList(Stage primaryStage) {
        ListView<VBox> listView = new ListView<>();
        ObservableList<VBox> items = FXCollections.observableArrayList();

        for (SportsTeam team : teamList) {
            VBox vbox = new VBox(10);
            Text details = new Text(team.getDetails());

            Pane logoPane = team.getLogo();
            logoPane.setPrefSize(100, 100);
            vbox.getChildren().addAll(logoPane, details);
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

    private void showSearchScreen(Stage primaryStage) {
        VBox vbox = new VBox(10);

        Label searchLabel = new Label("Enter Team ID to Search:");
        TextField searchField = new TextField();
        searchField.setPromptText("Team ID");

        Button searchButton = new Button("Search");
        Button backButton = new Button("Back to Main Menu");

        searchButton.setOnAction(event -> {
            try {
                int teamID = Integer.parseInt(searchField.getText().trim());
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

    private void saveTeams() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("teams.dat"))) {
            oos.writeObject(teamList);
            System.out.println("Teams saved successfully.");
        } catch (IOException e) {
            showErrorDialog("Error saving teams.");
        }
    }

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

    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.showAndWait();
    }


    private void showInfoDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
