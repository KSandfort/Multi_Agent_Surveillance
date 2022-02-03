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
    MapADT mapADT;
    MainLayout mainLayout;
    Scene mainScene;

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
        initScene();

        // Animation timer
        AnimationTimer timer = new MyTimer();
        timer.start();

        primaryStage.setTitle("Multi-Agent Simulation");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    private void initScene() {
        mainLayout = new MainLayout();
        mainScene = new Scene(mainLayout, 1200, 800);
    }

    public MainLayout getMainLayout() {
        return mainLayout;
    }

    public void updateGUI1Step() {
        // mainLayout.circle.relocate(mainLayout.circleX, 20);
        mainLayout.circleX += 5;
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
                Thread.sleep(400);
                // updateGUI1Step();
            }
            catch (Exception e) {
                System.out.println("GUI Thread error");
            }
            mainLayout.updateGUI(5);
        }
    }
}

