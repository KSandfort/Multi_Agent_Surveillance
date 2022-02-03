package gui;

import gui.sceneLayouts.MainLayout;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.MapADT;

/**
 * Parent class of all GUI elements.
 * Central unit to control the graphics.
 */
public class SimulationGUI extends Application {

    // Variables
    int currentStep;
    MapADT mapADT;
    MainLayout mainLayout;
    Scene mainScene;
    int simulationDelay;

    int circleX = 20; // temp var. should be deleted later

    public void setMapADT(MapADT map) {
        this.mapADT = map;
    }

    /**
     * Starts the GUI and produces a window
     */
    public void launchGUI() {

        String[] args = new String[0];
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        currentStep = 0;
        simulationDelay = 200;
        mainLayout = new MainLayout();
        mainScene = new Scene(mainLayout, 1200, 800);

        // Animation timer
        AnimationTimer timer = new MyTimer();
        timer.start();

        primaryStage.setTitle("Multi-Agent Simulation");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    /**
     * Updates the GUI one simulation step.
     */
    public void updateGUI1step() {
        mainLayout.circle.relocate(circleX, 20);
        circleX++;

        mainLayout.getStepCountLabel().setText("Current Step: " + currentStep);
        currentStep++;
    }

    public MainLayout getMainLayout() {
        return mainLayout;
    }

    /**
     * Timer to handle the game loop.
     * Acts as an inner class of the SimulationGUI.
     */
    private class MyTimer extends AnimationTimer {

        @Override
        public void handle(long now) {
            doHandle();
        }

        /**
         * Gets executed every frame.
         */
        private void doHandle() {
            try {
                Thread.sleep(simulationDelay); // Adds a delay
            }
            catch (Exception e) {
                System.out.println("GUI Thread Delay Error!");
            }
            updateGUI1step();
        }
    }
}

