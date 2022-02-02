package controller;

import gui.SimulationGUI;

/**
 * This class acts as the heart of the game. It controls all the parts
 * and is the point where GUI and back end come together.
 */
public class GameController {

    // Variables
    SimulationGUI simulationGUI;

    public GameController() {
        simulationGUI = new SimulationGUI();
        simulationGUI.launchGUI();
    }
}
