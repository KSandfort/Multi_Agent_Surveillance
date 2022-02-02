package controller;

import gui.SimulationGUI;
import model.MapADT;

/**
 * This class acts as the heart of the game. It controls all the parts
 * and is the point where GUI and back end come together.
 */
public class GameController {

    // Variables
    MapADT mapADT;
    SimulationGUI simulationGUI;

    public GameController() {
        mapADT = new MapADT();
        simulationGUI = new SimulationGUI();
        simulationGUI.setMapADT(mapADT);
        simulationGUI.launchGUI();
    }

    /**
     * Executes the loop to run the simulation
     */

}
