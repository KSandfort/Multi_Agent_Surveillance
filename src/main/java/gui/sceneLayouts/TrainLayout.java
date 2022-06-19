package gui.sceneLayouts;

import gui.TableDataModel;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;
import model.neural_network.NNTraining;

/**
 * Layout to display the current training status.
 */
@Getter
@Setter
public class TrainLayout extends BorderPane {

    // Variables
    TableView table;
    private ToggleGroup trainToggle;
    private RadioButton loadExisting;
    private RadioButton trainNew;
    private boolean startFromFile = true;
    private ToggleGroup agentToggle;
    private RadioButton guardTraining;
    private RadioButton intruderTraining;
    private ComboBox enemyAgentBox;
    private ObservableList<String> enemyAgent =
            FXCollections.observableArrayList(
                    "Random Agent",
                    "Remote Agent",
                    "Bug Agent",
                    "Intruder Destroyer",
                    "Ant Agent",
                    "NEAT Agent"
            );

    private ComboBox mapChoiceBox;
    private ObservableList<String> mapChoice =
            FXCollections.observableArrayList(
                    "1",
                    "2",
                    "3"
            );
    Button startTrainingButton;

    // Static
    public static boolean active = false;
    public static TableDataModel trainingInfo; // Agent side (guard, intruder) and number of agents
    public static TableDataModel totalGenerations;
    public static TableDataModel generationCount;
    public static TableDataModel currentStep;
    public static TableDataModel totalGamesPlayed;
    public static TableDataModel totalWinsGuards;
    public static TableDataModel totalWinsIntruders;
    public static TableDataModel totalDraws;
    public static TableDataModel totalWinRatio;
    public static TableDataModel bestFitness;
    public static int gameCount = 0;
    public static int guardWins = 0;
    public static int intruderWins = 0;
    public static int draws = 0;

    /**
     * Constructor
     */
    public TrainLayout() {
        this.setStyle("-fx-font: 12px 'Verdana';");
        active = true;
        initComponents();
    }

    /**
     * Inits all components on this layout.
     */
    private void initComponents() {
        // Radio buttons
        trainToggle = new ToggleGroup();
        loadExisting = new RadioButton("Load training from file");
        loadExisting.setToggleGroup(trainToggle);
        loadExisting.setSelected(true);
        trainNew = new RadioButton("New training");
        trainNew.setToggleGroup(trainToggle);

        agentToggle = new ToggleGroup();
        guardTraining = new RadioButton("Train Guards");
        guardTraining.setToggleGroup(agentToggle);
        guardTraining.setSelected(true);
        intruderTraining = new RadioButton("Train Intruders");
        intruderTraining.setToggleGroup(agentToggle);

        enemyAgentBox = new ComboBox(enemyAgent);
        enemyAgentBox.getSelectionModel().selectFirst();

        mapChoiceBox = new ComboBox(mapChoice);
        mapChoiceBox.getSelectionModel().selectFirst();

        VBox radioButtonBox = new VBox();
        radioButtonBox.setSpacing(5);
        radioButtonBox.setPadding(new Insets(5, 5, 5, 5));
        radioButtonBox.getChildren().addAll(
                new Label("Start of the training:"),
                loadExisting,
                trainNew,
                new Label("Train the following side (team):"),
                guardTraining,
                intruderTraining,
                new Label("Enemy Agent"),
                enemyAgentBox,
                new Label("Map Selection"),
                mapChoiceBox
        );
        this.setTop(radioButtonBox);

        // Create data table
        table = new TableView();
        table.setEditable(false);

        TableColumn<TableDataModel, String> col1 = new TableColumn<>("Name");
        TableColumn<TableDataModel, Double> col2 = new TableColumn<>("Value");

        col1.setCellValueFactory(new PropertyValueFactory<>("name"));
        col2.setCellValueFactory(new PropertyValueFactory<>("value"));

        table.getColumns().addAll(col1, col2);

        // Add table data
        trainingInfo = new TableDataModel("GUARD/INTRUDER", 0.0);
        totalGenerations = new TableDataModel("Total Generations", 1000.0);
        generationCount = new TableDataModel("Current Generation", 0.0);
        currentStep = new TableDataModel("Game Tick", 0.0);
        totalGamesPlayed = new TableDataModel("Games Played (total)", (double) gameCount);
        totalWinsGuards = new TableDataModel("Guard wins", 0.0);
        totalWinsIntruders = new TableDataModel("Intruder wins", 0.0);
        totalDraws = new TableDataModel("Draws", 0.0);

        this.setCenter(table);

        // Start button
        startTrainingButton = new Button("Start Training");
        startTrainingButton.setPrefHeight(90);
        startTrainingButton.setPrefWidth(120);
        startTrainingButton.setOnAction(e -> {
            startTraining();
        });
        VBox bottomBox = new VBox();
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(5, 5, 5, 5));
        bottomBox.getChildren().add(startTrainingButton);
        this.setBottom(bottomBox);

        Timeline refreshTimer = new Timeline(
                new KeyFrame(Duration.seconds(0.1),
                        e -> {
                            table.refresh();
                            totalGamesPlayed.setValue((double) gameCount);
                            totalWinsGuards.setValue((double) guardWins);
                            totalWinsIntruders.setValue((double) intruderWins);
                            totalDraws.setValue((double) draws);
                        })
        );
        refreshTimer.setCycleCount(Timeline.INDEFINITE);
        refreshTimer.play();
    }

    /**
     * Method to start the training once the start-button is clicked.
     */
    public void startTraining() {
        // Set NN variables
        if (loadExisting.isSelected()) {
            startFromFile = true;
        }

        int enemyAgentType = 0;
        if (enemyAgentBox.getValue().equals("Random Agent")) {
            enemyAgentType = 0;
        }
        if (enemyAgentBox.getValue().equals("Remote Agent")) {
            enemyAgentType = 1;
        }
        if (enemyAgentBox.getValue().equals("Bug Agent")) {
            enemyAgentType = 2;
        }
        if (enemyAgentBox.getValue().equals("Intruder Destroyer")) {
            enemyAgentType = 3;
        }
        if (enemyAgentBox.getValue().equals("Ant Agent")) {
            enemyAgentType = 4;
        }
        if (enemyAgentBox.getValue().equals("NEAT Agent")) {
            enemyAgentType = 5;
        }
        if (guardTraining.isSelected()) {
            NNTraining.trainGuard = true;
            NNTraining.guardType = 5;
            NNTraining.intruderType = enemyAgentType;
            trainingInfo.setName("Guard Training");
        }
        else {
            NNTraining.trainGuard = false;
            NNTraining.guardType = enemyAgentType;
            NNTraining.intruderType = 5;
            trainingInfo.setName("Intruder Training");
        }
        String mapPath = "src/main/resources/maps/phase2_" + mapChoiceBox.getValue() + ".txt";
        NNTraining.mapPath = mapPath;

        // Add entries
        table.getItems().addAll(
                trainingInfo,
                totalGenerations,
                generationCount,
                currentStep,
                totalGamesPlayed,
                totalWinsGuards,
                totalWinsIntruders,
                totalDraws
        );


        Thread trainingThread = new Thread(() -> {
            NNTraining nnTraining = new NNTraining();
            nnTraining.train(1000, startFromFile);
        });
        trainingThread.start();

    }
}
