package gui;

import controller.GameController;
import gui.sceneLayouts.MainLayout;
import gui.sceneLayouts.StartLayout;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;

/**
 * Parent class of all GUI elements.
 * Central unit to control the graphics.
 */
@Getter
@Setter
public class SimulationGUI extends Application {

    // Variables
    private int currentStep;
    private StartLayout startLayout;
    private MainLayout mainLayout;
    private Scene mainScene;
    private int simulationDelay;
    private Timeline timeline;
    private GameController controller;
    private final int FPS = 20;
    private final int WIDTH = 1200;
    private final int HEIGHT = 800;

    public static final int CANVAS_OFFSET = 50; // Pushes the map a bit in the middle of the canvas (x and y).
    public static double SCALING_FACTOR = 10;

    public void launchGUI() {
        String[] args = new String[0];
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        startTitleScreenGUI(primaryStage);
    }

    /**
     * Launches the start screen GUI
     * @param primaryStage
     */
    public void startTitleScreenGUI(Stage primaryStage) {
        startLayout = new StartLayout(primaryStage);
        startLayout.setSimulationGUI(this);
        mainScene = new Scene(startLayout, 700, 500);
        primaryStage.setTitle("Multi-Agent Simulation");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    /**
     * Launches the actual game GUI (with guard and intruder amount from the start screen)
     * @param primaryStage
     * @param guardAmount
     * @param intruderAmount
     */
    public void startSimulationGUI(Stage primaryStage, int guardAmount, int intruderAmount, int mapCode) {
        currentStep = 0;
        simulationDelay = 0;
        mainLayout = new MainLayout(primaryStage);
        mainLayout.setSimulationGUI(this);
        mainScene = new Scene(mainLayout, 1800, 1000);

        GameController.amountOfGuards = guardAmount;
        GameController.amountOfIntruders = intruderAmount;

        this.setController(new GameController(this, mapCode));
        this.controller.drawFixedItems(mainLayout);

        // Timeline Animation
        this.timeline = new Timeline(new KeyFrame(Duration.millis(1000/FPS), actionEvent -> updateGUI1step()));
        this.timeline.setCycleCount(Timeline.INDEFINITE);
        // Display Window
        primaryStage.setTitle("Multi-Agent Simulation");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    /**
     * Updates the GUI one simulation step.
     */
    public void updateGUI1step() {
        this.controller.update();
        mainLayout.getStepCountLabel().setText("Current Step: " + currentStep);
        this.controller.drawMovingItems(mainLayout);
        currentStep++;
    }

    /**
     * Starts the simulation.
     */
    public void startSimulation() {
        if (GameController.terminalFeedback)
            System.out.println("Simulation is starting!");
        this.timeline.play();
    }

    /**
     * Stops the simulation.
     */
    public void stopSimulation() {
        if (GameController.terminalFeedback)
            System.out.println("Simulation ended at step: " + getCurrentStep());
        this.timeline.stop();
    }

    /**
     * Pauses the simulation
     */
    public void pauseSimulation() {
        this.timeline.pause();
    }

}
