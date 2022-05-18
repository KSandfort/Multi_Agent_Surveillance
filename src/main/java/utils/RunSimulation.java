package utils;

import controller.GameController;
import gui.SimulationGUI;
import gui.sceneLayouts.MainLayout;
import model.GameMap;

/**
 * Runs a simulation with given parameters
 */
public class RunSimulation {

    /**
     * Trigger method for a simulation
     */
    public static void run(boolean gui,
                           boolean terminalFeedback,
                           String path,
                           int guardType,
                           int intruderType) {
        SimulationGUI.bypassMenu = true;
        SimulationGUI.bypassPath = path;
        GameController.guardAgentType = guardType;
        GameController.intruderAgentType = intruderType;
        SimulationGUI.autoStart = true;
        if (gui) {
            SimulationGUI sim = new SimulationGUI();
            sim.launchGUI();
        }
        if (terminalFeedback) {
            GameController.terminalFeedback = true;
        }
    }


    public static void main(String[] args) {
        run(true, true, "src/main/resources/maps/phase2_1.txt", 5, 0);
    }
}
