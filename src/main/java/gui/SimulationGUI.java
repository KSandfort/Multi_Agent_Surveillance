package gui;

import gui.sceneLayouts.MainLayout;
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
        primaryStage.setTitle("Multi-Agent Simulation");
        mainLayout = new MainLayout();
        mainScene = new Scene(mainLayout, 1200, 800);
        primaryStage.setScene(mainScene);
        primaryStage.show();
        System.out.println("Test");
    }

    public MainLayout getMainLayout() {
        return mainLayout;
    }
}
