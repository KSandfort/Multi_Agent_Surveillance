package gui.sceneLayouts;

import gui.TableDataModel;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
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
    public static TableDataModel totalWinRatio;
    public static TableDataModel bestFitness;

    public static int gameCount = 0;


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

        // Create data table
        table = new TableView();
        table.setEditable(false);

        TableColumn<TableDataModel, String> col1 = new TableColumn<>("Name");
        TableColumn<TableDataModel, Double> col2 = new TableColumn<>("Value");

        col1.setCellValueFactory(new PropertyValueFactory<>("name"));
        col2.setCellValueFactory(new PropertyValueFactory<>("value"));

        table.getColumns().addAll(col1, col2);

        // Add entries
        trainingInfo = new TableDataModel("GUARD/INTRUDER", 10.0); //TODO: get correct information
        totalGenerations = new TableDataModel("Total Generations", 1000.0);
        generationCount = new TableDataModel("Current Generation", 0.0);
        currentStep = new TableDataModel("Game Tick", 0.0);
        totalGamesPlayed = new TableDataModel("Games Played (total)", (double) gameCount);

        table.getItems().addAll(
                totalGenerations,
                generationCount,
                currentStep
        );

        this.setCenter(table);

        // Start button
        startTrainingButton = new Button("Start Training");
        startTrainingButton.setPrefHeight(90);
        startTrainingButton.setPrefWidth(120);
        startTrainingButton.setOnAction(e -> {
            startTraining();
        });

        this.setBottom(startTrainingButton);

        Timeline refreshTimer = new Timeline(
                new KeyFrame(Duration.seconds(0.1),
                        e -> {
                            table.refresh();
                            totalGamesPlayed.setValue((double) gameCount);
                        })
        );
        refreshTimer.setCycleCount(Timeline.INDEFINITE);
        refreshTimer.play();

    }

    public void startTraining() {
        Thread trainingThread = new Thread(() -> {
            NNTraining nnTraining = new NNTraining();
            nnTraining.train(1000, false);
        });
        trainingThread.start();
    }
}
