package gui.sceneLayouts;

import gui.SimulationGUI;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * This class represents the main layout, that contains a visual
 * representation of the map.
 */
public class StartLayout extends BorderPane {

    // Variables
    SimulationGUI simulationGUI;
    Pane canvas;
    Label projectLabel;
    Button startTestMap;
    boolean isPlaying;
    public static int yOffset = 50;

    TextField guardAmount;
    TextField intruderAmount;

    Button startButton;

    int amountOfGuards = 1;
    int amountOfIntruders = 1;

    public Circle circle;
    public int circleX = 20;

    Stage primaryStage;

    ObservableList<String> mapsList =
            FXCollections.observableArrayList(
                    "Test map"
            );

    ComboBox map;

    /**
     * Constructor
     */
    public StartLayout(Stage primaryStage) {
        this.setStyle("-fx-font: 12px 'Verdana';");
        isPlaying = false;
        initComponents();

        this.primaryStage = primaryStage;
    }

    /**
     * Creates all components on the main layout.
     */
    public void initComponents() {
        // Canvas - Center
        canvas = new Pane();
        canvas.setStyle("-fx-background-color: gray;");
        canvas.setPrefSize(1200, 800);

        this.setCenter(canvas);

        // Info - Top
        HBox infoBox = new HBox();
        infoBox.setSpacing(20);
        infoBox.setAlignment(Pos.CENTER);
        infoBox.setPrefHeight(50);

        projectLabel = new Label("Project 2-2: Group 3");
        infoBox.getChildren().setAll(projectLabel);

        this.setTop(infoBox);

        // Controls - Bottom
        HBox controlsContainer = new HBox();
        controlsContainer.setSpacing(20);
        controlsContainer.setAlignment(Pos.CENTER);
        controlsContainer.setPrefHeight(yOffset);
        startTestMap = new Button("Start test map");

        startTestMap.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    final Stage dialog = new Stage();
                    dialog.initModality(Modality.APPLICATION_MODAL);
                    dialog.setTitle("Configure map");
                    dialog.setResizable(false);
                    dialog.initOwner(primaryStage);
                    VBox dialogVbox = new VBox(20);

                    dialogVbox.setPadding(new Insets(10, 50, 50, 50));
                    dialogVbox.setSpacing(5);

                    map = new ComboBox(mapsList);
                    map.getSelectionModel().selectFirst();

                    Label guardLabel = new Label("Guard amount");
                    Label intruderLabel = new Label("Intruder amount");
                    guardAmount = new TextField("10");
                    intruderAmount = new TextField("10");

                    guardAmount.textProperty().addListener(new NumericInputEnforcer(guardAmount));
                    intruderAmount.textProperty().addListener(new NumericInputEnforcer(intruderAmount));

                    startButton = new Button("Start");
                    startButton.setOnAction(e -> {
                        simulationGUI.startSimulationGUI(primaryStage, Integer.parseInt(guardAmount.getText()), Integer.parseInt(intruderAmount.getText()));
                        dialog.close();
                    });

                    dialogVbox.getChildren().addAll(guardLabel, guardAmount, intruderLabel, intruderAmount, new Label("Map"), map, startButton);
                    Scene dialogScene = new Scene(dialogVbox, 300, 200);
                    dialog.setScene(dialogScene);
                    dialog.show();
                }
            });
            // simulationGUI.startSimulationGUI(primaryStage);

        controlsContainer.getChildren().addAll(startTestMap);
        this.setBottom(controlsContainer);
    }

    public Pane getCanvas() {
        return this.canvas;
    }

    public Label getStepCountLabel() {
        return projectLabel;
    }

    public void setSimulationInstance(SimulationGUI simulationGUI) {
        this.simulationGUI = simulationGUI;
    }
}

class NumericInputEnforcer implements ChangeListener<String> {
    TextField myTextField;

    NumericInputEnforcer(TextField textField) {
        myTextField = textField;
    }

    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (!newValue.matches("\\d{0,4}?")) {
            myTextField.setText(oldValue);
        }
    };
}