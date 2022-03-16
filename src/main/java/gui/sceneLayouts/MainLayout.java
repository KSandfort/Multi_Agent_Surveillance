package gui.sceneLayouts;

import gui.ExplorationStage;
import gui.SimulationGUI;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

/**
 * This class represents the main layout, that contains a visual
 * representation of the map.
 */
@Getter
@Setter
public class MainLayout extends BorderPane {

    // Variables
    private SimulationGUI simulationGUI;
    private Pane canvas;
    private Label stepCountLabel;
    private Button playPauseButton;
    private Button stepButton;
    private Button returnToStartButton;
    private Button explorationButton;
    private Slider simSpeedSlider;
    private Label speedLabel;
    private boolean isPlaying;
    private Stage primaryStage;
    public static int yOffset = 50;

    /**
     * Constructor
     */
    public MainLayout(Stage primaryStage) {
        this.setStyle("-fx-font: 12px 'Verdana';");
        this.primaryStage = primaryStage;
        isPlaying = false;
        initComponents();
    }

    /**
     * Creates all components on the main layout.
     */
    public void initComponents() {
        // Canvas - Center
        canvas = new Pane();
        canvas.setStyle("-fx-background-color: rgb(200, 220, 200);");
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
        playPauseButton = new Button("Play / Pause");
        playPauseButton.setOnAction(event -> {
            if (isPlaying) {
                isPlaying = false;
                simulationGUI.pauseSimulation();
            }
            else {
                isPlaying = true;
                simulationGUI.startSimulation();
            }
        });
        stepButton = new Button("Step");
        int i = 50;
        stepButton.setOnAction(e -> {
            if (!isPlaying) {
                simulationGUI.updateGUI1step();
            }
        });
        simSpeedSlider = new Slider();
        simSpeedSlider.setMin(1);
        simSpeedSlider.setMax(1000);
        speedLabel = new Label("Simulation Speed:");

        returnToStartButton = new Button("Return to start");
        returnToStartButton.setOnAction(e -> {
           // simulationGUI.startTitleScreenGUI(primaryStage);
            System.out.println("Todo...");
        });

        explorationButton = new Button("See Exploration");
        explorationButton.setOnAction(e -> {
            ExplorationStage explorationStage = new ExplorationStage(this);
        });

        controlsContainer.getChildren().addAll(
                playPauseButton,
                stepButton,
                speedLabel,
                simSpeedSlider,
                returnToStartButton,
                explorationButton);
        this.setBottom(controlsContainer);
    }
}
