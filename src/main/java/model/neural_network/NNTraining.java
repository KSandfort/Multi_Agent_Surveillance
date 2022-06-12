package model.neural_network;

import java.io.FileWriter;
import java.io.IOException;

public class NNTraining {
    final static int generations = 1;//number of generations to train
    final static int guardType = 0;
    final static int intruderType = 5;
    final static boolean trainGuard = false;//set to true if your training a guard, for intruder set to false
    final static boolean trainOn3Maps = false;//set to true if you want to train using all 3 maps of this phase
    final static int generationBetweenSave = 20;
    final static String mapPath = "src/main/resources/maps/phase2_1.txt";//map to be used if your not training on all 3 maps

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
