package model.neural_network;

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
    final static int generations = 1000; // Number of generations to train
    public static int guardType = 5;
    public static int intruderType = 2;
    public static boolean trainGuard = true; // Set to true if you're training a guard, for intruder set to false
    final static boolean trainOn3Maps = false; // Set to true if you want to train using all 3 maps of this phase
    public static boolean startFromNothing = false; // Set this to false if you want to start with a fully connected network
    final static int generationThreads = 4; // Splits the gene pool into equally-sized partitions for faster training
    final static int generationBetweenSave = 20;
    public static String networkFilePath = "output/Neat results/bestNetwork.txt";//file path to save network to
    public static String simulationVarsFilePath = "output/Neat results/fromPreviousSim.txt";//file path to save simulation variables to
    public static String fitnessFilePath = "output/Neat results/fitness.txt";//file path to save fitness score
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
        if ((g.generation - 1) % generationBetweenSave == 0)
        {
            String[] networkStrings = networkFilePath.split("\\.");
            String[] simStrings = simulationVarsFilePath.split("\\.");

            nn.saveNetwork(networkStrings[0] + g.generation + "." + networkStrings[1]);
            nn.saveGlobals(simStrings[0] + g.generation + "." + simStrings[1]);
        }
        nn.saveNetwork(networkFilePath);
        nn.saveGlobals(simulationVarsFilePath);
    }

    private void saveFitness(GenePool g) {
        try {
            FileWriter fw = new FileWriter(fitnessFilePath,true);
            fw.write("\n" + g.generation + ";" + g.bestNetwork.getFitness());
            fw.flush();
            fw.close();
        } catch (
                IOException exception) {
            System.out.println("Error writing to file:\n" + exception);
        }
    }
}
