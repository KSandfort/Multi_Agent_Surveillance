package gui.sceneLayouts;

import gui.SimulationGUI;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * This class represents the start layout.
 */
public class StartLayout extends BorderPane {

    // Variables
    SimulationGUI simulationGUI;
    Label projectLabel;
    Button startButton;
    TextField guardAmount;
    TextField intruderAmount;
    Stage primaryStage;
    GridPane mainGrid;
    int gameMode; // 0 = exploration, 1 = guards vs intruders
    ObservableList<String> mapsList =
            FXCollections.observableArrayList(
                    "Test map",
                    "Read from file"
            );
    ComboBox mapListBox;

    /**
     * Constructor
     */
    public StartLayout(Stage primaryStage) {
        this.setStyle("-fx-font: 12px 'Verdana';");
        initComponents();
        this.primaryStage = primaryStage;
    }

    /**
     * Creates all components on the main layout.
     */
    public void initComponents() {

        // Main Controls - Center

        mainGrid = new GridPane();

        // --- Game Mode Row ---
        gameMode = 0; // Default Game Mode
        Label gameModeLabel = new Label("Gamemode");
        gameModeLabel.setStyle("-fx-font: 12px 'Verdana';");
        HBox gameModeButtonBox = new HBox();
        Button gameModeButton1 = new Button("Exploration");
        Button gameModeButton2 = new Button("Guards vs Intruders.");
        gameModeButton1.setStyle("-fx-font: 12px 'Verdana';");
        gameModeButton2.setStyle("-fx-font: 12px 'Verdana';");
        gameModeButton1.setDisable(true);
        gameModeButton1.setOnAction(e -> {
            gameMode = 0;
            gameModeButton1.setDisable(true);
            gameModeButton2.setDisable(false);
            intruderAmount.setDisable(true);
        });
        gameModeButton2.setOnAction(e -> {
            gameMode = 1;
            gameModeButton1.setDisable(false);
            gameModeButton2.setDisable(true);
            intruderAmount.setDisable(false);
        });
        gameModeButtonBox.setSpacing(10);
        gameModeButtonBox.getChildren().addAll(gameModeButton1, gameModeButton2);
        mainGrid.add(gameModeLabel, 0, 0);
        mainGrid.add(gameModeButtonBox, 1, 0);

        // --- Guards Row ---
        Label guardLabel = new Label("Guard amount");
        guardLabel.setStyle("-fx-font: 12px 'Verdana';");
        guardAmount = new TextField("3");
        guardAmount.textProperty().addListener(new NumericInputEnforcer(guardAmount));
        mainGrid.add(guardLabel, 0, 1);
        mainGrid.add(guardAmount, 1, 1);

        // --- Intruders Row ---
        Label intruderLabel = new Label("Intruder amount");
        intruderAmount = new TextField("3");
        intruderAmount.setDisable(true);
        intruderAmount.textProperty().addListener(new NumericInputEnforcer(intruderAmount));
        mainGrid.add(intruderLabel, 0, 2);
        mainGrid.add(intruderAmount, 1, 2);

        // --- Map Row ---
        Label mapLabel = new Label("Map");
        mapLabel.setStyle("-fx-font: 12px 'Verdana';");
        mapListBox = new ComboBox(mapsList);
        mapListBox.getSelectionModel().selectFirst();
        mainGrid.add(mapLabel, 0, 3);
        mainGrid.add(mapListBox, 1, 3);

        // Add mainGrid
        mainGrid.setPadding(new Insets(10, 10, 10, 10));
        mainGrid.setHgap(10);
        mainGrid.setVgap(10);
        this.setCenter(mainGrid);

        // Info - Top
        HBox infoBox = new HBox();
        infoBox.setSpacing(20);
        infoBox.setAlignment(Pos.CENTER);
        infoBox.setPrefHeight(50);
        projectLabel = new Label("Project 2-2: Group 3");
        projectLabel.setStyle("-fx-font: 24px 'Verdana';");
        infoBox.getChildren().setAll(projectLabel);
        this.setTop(infoBox);

        // Controls - Bottom
        HBox controlsBox = new HBox();
        controlsBox.setAlignment(Pos.CENTER);
        controlsBox.setPadding(new Insets(10, 10, 10, 10));
        startButton = new Button("Start");
        startButton.setPrefHeight(90);
        startButton.setPrefWidth(120);
        startButton.setOnAction(e -> {
            // Determine number of guards/intruders
            int amountGuards = Integer.parseInt(guardAmount.getText());
            int amountIntruders;
            if (gameMode == 0) {
                amountIntruders = 0;
            }
            else {
                amountIntruders = Integer.parseInt(intruderAmount.getText());
            }
            // Determine selected map
            int mapCode;
            if (mapListBox.getValue().equals("Test map")) {
                mapCode = 0;
            }
            if (mapListBox.getValue().equals("Read from file")) {
                mapCode = 1;
            }
            else {
                mapCode = 0;
            }
            simulationGUI.startSimulationGUI(primaryStage, amountGuards, amountIntruders, mapCode);
        });
        controlsBox.getChildren().add(startButton);
        this.setBottom(controlsBox);
    }

    public Label getStepCountLabel() {
        return projectLabel;
    }

    public void setSimulationInstance(SimulationGUI simulationGUI) {
        this.simulationGUI = simulationGUI;
    }

    public int getGameMode(){return gameMode; }
}

/**
 * Class for a numeric input that is used for the text fields in the start screen.
 */
class NumericInputEnforcer implements ChangeListener<String> {

    // Variables
    TextField myTextField;

    /**
     * Constructor
     * @param textField
     */
    NumericInputEnforcer(TextField textField) {
        myTextField = textField;
    }

    /**
     * ChangeListener method
     * @param observable
     * @param oldValue
     * @param newValue
     */
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (!newValue.matches("\\d{0,4}?")) {
            myTextField.setText(oldValue);
        }
    };
}