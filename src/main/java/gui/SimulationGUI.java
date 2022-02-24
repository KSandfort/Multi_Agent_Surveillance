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

/**
 * Parent class of all GUI elements.
 * Central unit to control the graphics.
 */
public class SimulationGUI extends Application {

    // Variables
    int currentStep;
    StartLayout startLayout;
    MainLayout mainLayout;
    Scene mainScene;
    int simulationDelay;
    Timeline timeline;
    GameController controller;
    final int FPS = 10;
    final int WIDTH = 1200;
    final int HEIGHT = 800;
    public static final int CANVAS_OFFSET = 50; // Pushes the map a bit in the middle of the canvas (x and y).
    public static double SCALING_FACTOR = 10;

    public void setController(GameController controller){
        this.controller = controller;
    }

    public void launchGUI() {
        String[] args = new String[0];
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        startTitleScreenGUI(primaryStage);
    }

    // Launches the start screen GUI
    public void startTitleScreenGUI(Stage primaryStage) {

        startLayout = new StartLayout(primaryStage);
        startLayout.setSimulationInstance(this);
        mainScene = new Scene(startLayout, 1300, 1000);
        primaryStage.setTitle("Multi-Agent Simulation");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    // Launches the actual game GUI (with guard and intruder amount from the start screen)
    public void startSimulationGUI(Stage primaryStage, int guardAmount, int intruderAmount) {
        currentStep = 0;
        simulationDelay = 1;
        mainLayout = new MainLayout(primaryStage);
        mainLayout.setSimulationInstance(this);
        mainScene = new Scene(mainLayout, 1300, 1000);

        GameController.amountOfGuards = guardAmount;
        GameController.amountOfIntruders = intruderAmount;

        this.setController(new GameController(this));
        this.controller.drawFixedItems(mainLayout);



        // Timeline Animation
        this.timeline = new Timeline(new KeyFrame(Duration.millis(1000/FPS), actionEvent -> update()));
        this.timeline.setCycleCount(Timeline.INDEFINITE);
        // Display Window
        primaryStage.setTitle("Multi-Agent Simulation");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    public void update() {
        controller.update();
        updateGUI1step();
    }

    /**
     * Starts the simulation.
     */
    public void startSimulation() {
        this.timeline.play();
    }

    /**
     * Stops the simulation.
     */
    public void stopSimulation() {
        this.timeline.stop();
    }

    /**
     * Pauses the simulation
     */
    public void pauseSimulation() {
        this.timeline.pause();
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

    public Scene getMainScene() {
        return mainScene;
    }

    public MainLayout getMainLayout() {
        return mainLayout;
    }

    public void setMainScene(Scene mainScene) {
        this.mainScene = mainScene;
    }

    public int getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(int currentStep) {
        this.currentStep = currentStep;
    }

}
