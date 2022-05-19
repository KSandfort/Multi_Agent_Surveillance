package model.neural_network;

import java.io.FileWriter;
import java.io.IOException;

public class NNTraining {
    final static int generations = 10;
    final static int guardType = 0;
    final static int intruderType = 5;
    final static boolean trainGuard = true;
    final static String mapPath = "src/main/resources/maps/phase2_1.txt";

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
        nn.saveNetwork("bestNetwork.txt");
        nn.saveGlobals("fromPreviousSim.txt");
    }

    private void saveFitness(GenePool g) {
        try {
            FileWriter fw = new FileWriter("fitness.txt",true);
            fw.write("\n" + g.generation + ";" + g.bestNetwork.getFitness());
            fw.flush();
            fw.close();
        } catch (
                IOException exception) {
            System.out.println("Error writing to file:\n" + exception);
        }
    }
}
