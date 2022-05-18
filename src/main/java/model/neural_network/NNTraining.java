package model.neural_network;

public class NNTraining
{
    final static int generations = 8;

    public static void main(String[] args) {
        NNTraining nnt = new NNTraining();
        nnt.train(generations,false);
    }

    public void train(int n,boolean startFromFile)
    {
        GenePool genePool = new GenePool(-1,-1);
        genePool.init(startFromFile);

        //run the genetic algorithm for n generations
        for(int i = 0; i < n; i++)
        {
            genePool.newGeneration();
            saveNetwork(genePool);
        }
    }

    private void saveNetwork(GenePool g)
    {
        NeuralNetwork nn = g.bestNetwork;
        nn.saveNetwork("bestNetwork.txt");
        nn.saveGlobals("fromPreviousSim.txt");
    }
}
