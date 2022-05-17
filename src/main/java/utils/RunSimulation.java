package utils;

import controller.GameController;
import gui.SimulationGUI;

/**
 * Runs a simulation with given parameters
 */
public class RunSimulation {

    /**
     * Trigger method for a simulation
     */
    public static void run(boolean gui, boolean terminalFeedback) {
        if (gui) {
            SimulationGUI sim = new SimulationGUI();
            sim.launchGUI();
        }
        if (terminalFeedback) {
            GameController.terminalFeedback = true;
        }
    }


    public static void main(String[] args) {
        run(true, true);
    }
}
