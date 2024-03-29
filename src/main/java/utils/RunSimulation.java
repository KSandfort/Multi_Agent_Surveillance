package utils;

import agents.NeatAgent;
import controller.GameController;
import gui.SimulationGUI;
import model.neural_network.NNTraining;
import model.neural_network.NeuralNetwork;

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
        NeuralNetwork.readGlobals(NNTraining.simulationVarsFilePath);
        NeatAgent.setNn(NeuralNetwork.readNetwork(NNTraining.networkFilePath));
        SimulationGUI.autoStart = true;
        if (gui) {
            SimulationGUI sim = new SimulationGUI();
            sim.launchGUI();
        }
        if (terminalFeedback) {
            GameController.terminalFeedback = true;
        }
    }

    /**
     * Main method for debugging purposes.
     * @param args
     */
    public static void main(String[] args) {
        run(true, true, "src/main/resources/maps/phase2_1.txt", 0, 5);
    }
}
