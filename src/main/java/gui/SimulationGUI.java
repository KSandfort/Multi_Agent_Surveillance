package gui;

import controller.GameController;
import gui.sceneLayouts.MainLayout;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.GameMap;

import java.util.function.Consumer;

/**
 * Parent class of all GUI elements.
 * Central unit to control the graphics.
 */
public class SimulationGUI extends Application {

    // Variables
    int currentStep;
    GameMap map;
    MainLayout mainLayout;
    Scene mainScene;
    int simulationDelay;
    Timeline timeline;
    GameController controller;
    final int FPS = 2;
    final int WIDTH = 1200;
    final int HEIGHT = 800;

    int circleX = 20; // temp var. should be deleted later


    public void setController(GameController controller){
        this.controller = controller;
    }


    public void launchGUI() {
        String[] args = new String[0];
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        currentStep = 0;
        simulationDelay = 200;
        mainLayout = new MainLayout();
        mainLayout.setSimulationInstance(this);
        mainScene = new Scene(mainLayout, 1200, 800);
        this.setController(new GameController());
        // Timeline Animation
        this.timeline = new Timeline(new KeyFrame(Duration.millis(1000/FPS), actionEvent -> updateGUI1step()));
        this.timeline.setCycleCount(Timeline.INDEFINITE);
        // Display Window
        primaryStage.setTitle("Multi-Agent Simulation");
        primaryStage.setScene(mainScene);
        primaryStage.show();
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
        mainLayout.getStepCountLabel().setText("Current Step: " + currentStep);
        currentStep++;
        this.controller.drawMap(mainLayout);
    }

    public MainLayout getMainLayout() {
        return mainLayout;
    }

}
