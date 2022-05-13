package model.neural_network;

public class NNTraining
{
    public static void main(String[] args) {
        train();
    }


    public void train(int n)
    {
        GenePool genePool = new GenePool(-1,-1);
        genePool.init();

        //run the genetic algorithm for n generations
        for(int i = 0; i < n; i++)
        {
            genePool.newGeneration();
            saveBest(genePool);
        }
    }

    private void saveBest(GenePool g)
    {
        NeuralNetwork nn = g.bestNetwork;
    }
}
