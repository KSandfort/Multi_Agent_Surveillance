package model.neural_network;

import gui.sceneLayouts.TrainLayout;
import lombok.Getter;
import lombok.Setter;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Class that executes training for the NEAT agent.
 */
@Getter
@Setter
public class NNTraining {

    // Variables
    private TrainLayout trainLayout;
    final static int generations = 200; // Number of generations to train
    public static int guardType = 0;
    public static int intruderType = 5;
    public static boolean trainGuard = false; // Set to true if you're training a guard, for intruder set to false
    final static boolean trainOn3Maps = false; // Set to true if you want to train using all 3 maps of this phase
    final static int generationBetweenSave = 20;
    public static String mapPath = "src/main/resources/maps/phase2_1.txt"; // Map to be used if you're not training on all 3 maps

    /**
     * Execute to run training.
     * @param args
     */
    public static void main(String[] args) {
        NNTraining nnt = new NNTraining();
        nnt.train(generations, false);
    }

    public void train(int n, boolean startFromFile) {
        GenePool genePool = new GenePool(-1, -1);
        genePool.init(startFromFile);

        //run the genetic algorithm for n generations
        for (int i = 0; i < n; i++) {
            genePool.newGeneration();
            saveNetwork(genePool);
            saveFitness(genePool);
        }
    }

    private void saveNetwork(GenePool g) {
        NeuralNetwork nn = g.bestNetwork;
        if(g.generation - 1 % generationBetweenSave == 0)
        {
            nn.saveNetwork("output/Neat results/bestNetwork" + g.generation + ".txt");
            nn.saveGlobals("output/Neat results/fromPreviousSim" + g.generation +".txt");
        }
        nn.saveNetwork("output/Neat results/bestNetwork.txt");
        nn.saveGlobals("output/Neat results/fromPreviousSim.txt");
    }

    private void saveFitness(GenePool g) {
        try {
            FileWriter fw = new FileWriter("output/Neat results/fitness.txt",true);
            fw.write("\n" + g.generation + ";" + g.bestNetwork.getFitness());
            fw.flush();
            fw.close();
        } catch (
                IOException exception) {
            System.out.println("Error writing to file:\n" + exception);
        }
    }
}
