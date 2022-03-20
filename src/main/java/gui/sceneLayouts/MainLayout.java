package gui.sceneLayouts;

import gui.ExplorationStage;
import gui.SimulationGUI;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
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
    private Pane coverageCanvas;
    private Pane canvas;
    private Label stepCountLabel;
    private Button playPauseButton;
    private Button stepButton;
    private Button returnToStartButton;
    private Button explorationButton;
    private Slider simSpeedSlider;
    private Label speedLabel;
    private ProgressBar coverageBar;
    private TextField coverageText;
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
        canvas.setPrefSize(1200, 600);

        this.setCenter(canvas);

        // Info - Top
        HBox infoBox = new HBox();
        infoBox.setSpacing(20);
        infoBox.setAlignment(Pos.CENTER);
        infoBox.setPrefHeight(50);
        stepCountLabel = new Label("Current Step: 0");
        infoBox.getChildren().setAll(stepCountLabel);
        this.setTop(infoBox);

        // Coverage - Right
        VBox rightBox = new VBox();
        rightBox.setPrefWidth(500);
        rightBox.setSpacing(20);
        rightBox.setPadding(new Insets(30));
        coverageBar = new ProgressBar(0.0);
        coverageBar.setPrefWidth(300);
        coverageText = new TextField("0 %");
        coverageText.setPrefWidth(200);
        coverageText.setEditable(false);
        coverageCanvas = new Pane();
        coverageCanvas.setStyle("-fx-background-color: rgb(200, 220, 200);");
        coverageCanvas.setPrefWidth(400);
        coverageCanvas.setPrefHeight(300);
        rightBox.getChildren().addAll(new Label("Coverage:"), coverageBar, coverageText, coverageCanvas);
        this.setRight(rightBox);

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

    /**
     * Adds a point to the coverage monitoring
     * @param x x-pos
     * @param y y-pos
     * @param explored true if explored
     */
    public void addCoveragePoint(int x, int y, boolean explored) {
        Rectangle rect = new Rectangle(2, 2, Color.BLUE);
        rect.relocate(x * 2, y * 2);
        coverageCanvas.getChildren().add(rect);
    }
}
