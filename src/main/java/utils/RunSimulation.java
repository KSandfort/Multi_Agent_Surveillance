package utils;

import Enities.Intruder;
import agents.NeatAgent;
import controller.GameController;
import gui.SimulationGUI;
import gui.sceneLayouts.MainLayout;
import model.GameMap;
import model.neural_network.NNTraining;
import model.neural_network.NeuralNetwork;

/**
 * Runs a simulation with given parameters
 */
public class RunSimulation {

    static String[][] maps = {
            {
                "phase2_1.txt",
                "phase2_2.txt",
                "phase3_2.txt"
            },

            {
                "phase2_1.txt",
                "phase2_1_2_targets.txt",
                "phase2_1_4_targets.txt"
            }
    };




    /**
     * Trigger method for a simulation
     */
    public static String run(boolean gui,
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
        } else {
            int intruderAllTarget = 0;
            int intruder2Target = 0;
            int intruder1Target = 0;
            int guardWins = 0;
            int stalemate = 0;

            for(int i = 0; i < 10; i++) {
                System.out.print("Game " + i + "\r");
                GameController gc = GameController.simulate(2000, 3, 3, 3, 1);
                if (gc.getHasWonGame() == 1) {
                    guardWins++;
                } else if (gc.getHasWonGame() == 0){
                    stalemate++;
                }

                if (Intruder.intruderTargetCount == 1)
                    intruder1Target++;
                else if (Intruder.intruderTargetCount == 2)
                    intruder2Target++;
                else if (Intruder.intruderTargetCount == 3)
                    intruderAllTarget++;
            }

            return (guardWins + ", " + intruder1Target + ", " + intruder2Target + ", " + intruderAllTarget + ", " + stalemate);
        }
        if (terminalFeedback) {
            GameController.terminalFeedback = true;
        }

        return "";
    }


    static int runsCount = 10;

    static int guardType = 5;
    static int intruderType = 0;

    static int experiment = 1;  // 0 = win rates/efficiency, 1 = target count

    public static void main(String[] args) {

        String[] runs1 = new String[runsCount];
        String[] runs2 = new String[runsCount];
        String[] runs3 = new String[runsCount];


        for(int i = 0; i < runsCount; i++) {
            runs1[i] = run(false, true, "src/main/resources/maps/" + maps[experiment][0], guardType, intruderType);
            runs2[i] = run(false, true, "src/main/resources/maps/" + maps[experiment][1], guardType, intruderType);
            runs3[i] = run(false, true, "src/main/resources/maps/" + maps[experiment][2], guardType, intruderType);
        }

        // copy & paste this
        System.out.println("1 target");
        for(String r : runs1) {
            System.out.println(r);
        }
        System.out.println("2 targets");
        for(String r : runs2) {
            System.out.println(r);
        }
        System.out.println("4 targets");
        for(String r : runs3) {
            System.out.println(r);
        }
    }
}
