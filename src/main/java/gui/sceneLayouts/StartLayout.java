package gui.sceneLayouts;

import gui.SimulationGUI;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

/**
 * This class represents the main layout, that contains a visual
 * representation of the map.
 */
public class StartLayout extends BorderPane {

    // Variables
    SimulationGUI simulationGUI;
    Pane canvas;
    Label stepCountLabel;
    Button startTestMap;
    boolean isPlaying;
    public static int yOffset = 50;

    public Circle circle;
    public int circleX = 20;

    Stage primaryStage;

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
        stepCountLabel = new Label("Current Step: 0");
        infoBox.getChildren().setAll(stepCountLabel);
        this.setTop(infoBox);

        // Controls - Bottom
        HBox controlsContainer = new HBox();
        controlsContainer.setSpacing(20);
        controlsContainer.setAlignment(Pos.CENTER);
        controlsContainer.setPrefHeight(yOffset);
        startTestMap = new Button("Start test map");
        startTestMap.setOnAction(event -> {
            simulationGUI.startSimulationGUI(primaryStage);
        });

        controlsContainer.getChildren().addAll(startTestMap);
        this.setBottom(controlsContainer);
    }

    public Pane getCanvas() {
        return this.canvas;
    }

    public Label getStepCountLabel() {
        return stepCountLabel;
    }

    public void setSimulationInstance(SimulationGUI simulationGUI) {
        this.simulationGUI = simulationGUI;
    }
}
