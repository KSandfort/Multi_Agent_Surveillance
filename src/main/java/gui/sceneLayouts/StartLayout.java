package gui.sceneLayouts;

import controller.GameController;
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
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * This class represents the start layout.
 */
@Getter
@Setter
public class StartLayout extends BorderPane {

    // Variables
    private SimulationGUI simulationGUI;
    private Label projectLabel;
    private Button startButton;
    private TextField guardAmount;
    private TextField intruderAmount;
    private Stage primaryStage;
    private GridPane mainGrid;
    private int gameMode; // 0 = exploration, 1 = guards vs intruders

    File f = new File("src/main/resources/maps/");
    ArrayList<String> fileNames = new ArrayList<>(Arrays.asList(Objects.requireNonNull(f.list())));

    private CheckBox testMap;
    private CheckBox fileMap;
    private CheckBox randMap;
    private int mapCode;

    private ObservableList<String> mapsList =
            FXCollections.observableArrayList(
                    fileNames
            );
    private ComboBox mapListBox;

    private ObservableList<String> guardAgent =
            FXCollections.observableArrayList(
                    "Random Agent",
                    "Remote Agent",
                    "Bug Agent",
                    "Intruder Destroyer",
                    "Ant Agent",
                    "NEAT Agent"
            );
    private ComboBox guardAgentBox;

    private ObservableList<String> intruderAgent =
            FXCollections.observableArrayList(
                    "Random Agent",
                    "Remote Agent",
                    "Ant Agent",
                    "NEAT Agent"
            );
    private ComboBox intruderAgentBox;

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
        guardAgentBox = new ComboBox(guardAgent);
        guardAgentBox.getSelectionModel().selectFirst();
        mainGrid.add(guardLabel, 0, 1);
        mainGrid.add(guardAmount, 1, 1);
        mainGrid.add(guardAgentBox, 2, 1);

        // --- Intruders Row ---
        Label intruderLabel = new Label("Intruder amount");
        intruderAmount = new TextField("3");
        intruderAmount.setDisable(true);
        intruderAmount.textProperty().addListener(new NumericInputEnforcer(intruderAmount));
        intruderAgentBox = new ComboBox(intruderAgent);
        intruderAgentBox.getSelectionModel().selectFirst();
        mainGrid.add(intruderLabel, 0, 2);
        mainGrid.add(intruderAmount, 1, 2);
        mainGrid.add(intruderAgentBox, 2, 2);

        // --- Map Row ---
        Label mapLabel = new Label("Map");
        mapLabel.setStyle("-fx-font: 12px 'Verdana';");

        testMap = new CheckBox("Test map");
        randMap = new CheckBox("Random map");
        fileMap = new CheckBox("File map");


        mapListBox = new ComboBox(mapsList);
        mapListBox.getSelectionModel().selectFirst();
        mapListBox.setDisable(true);

        testMap.setOnAction(e -> {
            mapCode = 0;
            mapListBox.setDisable(true);
            randMap.setSelected(false);
            fileMap.setSelected(false);
        });
        randMap.setOnAction(e -> {
            mapCode = 2;
            mapListBox.setDisable(true);
            testMap.setSelected(false);
            fileMap.setSelected(false);
        });
        fileMap.setOnAction(e -> {
            mapCode = 1;
            mapListBox.setDisable(false);
            testMap.setSelected(false);
            randMap.setSelected(false);
        });

        HBox mapModeBox = new HBox();
        mapModeBox.getChildren().add(testMap);
        mapModeBox.getChildren().add(randMap);
        mapModeBox.getChildren().add(fileMap);
        mapModeBox.setSpacing(10);

        mainGrid.add(mapLabel, 0, 3);
        mainGrid.add(mapModeBox, 1, 3);
        mainGrid.add(mapListBox, 2, 3);

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
            // Determine agent type - Guard
            if (guardAgentBox.getValue().equals("Random Agent")) {
                GameController.guardAgentType = 0;
            }
            if (guardAgentBox.getValue().equals("Remote Agent")) {
                GameController.guardAgentType = 1;
            }
            if (guardAgentBox.getValue().equals("Bug Agent")) {
                GameController.guardAgentType = 2;
            }
            if (guardAgentBox.getValue().equals("Intruder Destroyer")) {
                GameController.guardAgentType = 3;
            }
            if (guardAgentBox.getValue().equals("Ant Agent")) {
                GameController.guardAgentType = 4;
            }
            if (guardAgentBox.getValue().equals("NEAT Agent")) {
                GameController.guardAgentType = 5;
            }

            // Determine agent type - Intruder
            if (intruderAgentBox.getValue().equals("Random Agent")) {
                GameController.intruderAgentType = 0;
            }
            if (intruderAgentBox.getValue().equals("Remote Agent")) {
                GameController.intruderAgentType = 1;
            }
            if (intruderAgentBox.getValue().equals("Ant Agent")) {
                GameController.intruderAgentType = 4;
            }
            if (intruderAgentBox.getValue().equals("NEAT Agent")) {
                GameController.intruderAgentType = 5;
            }
            simulationGUI.startSimulationGUI(primaryStage, amountGuards, amountIntruders, mapCode);
        });
        controlsBox.getChildren().add(startButton);
        this.setBottom(controlsBox);
    }
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