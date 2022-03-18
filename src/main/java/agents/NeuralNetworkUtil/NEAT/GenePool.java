package agents.NeuralNetworkUtil.NEAT;

import agents.NeuralNetworkUtil.NeuralNetwork;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GenePool
{
    final double distanceThreshold = 3.0;
    final double crossoverChance = 0.75;
    final int poolSize = 100;//population size. eg how many genomes in a generation
    List<Species> speciesList;
    int generation;
    double maxFitness;

    public GenePool()
    {
        speciesList =  new ArrayList<>();
        generation = 0;
        maxFitness = 0;
    }

    public void newGeneration()
    {
        cullSpecies(false);

        //rank genomes globally

        //remove stale species

        //calculate average fitness

        //remove weak species

        //breed based on shared fitness value

        //cull all but top member of each species
        cullSpecies(true);
        //add remaining population using top of species

        //add new genomes to species
        int childrenCount = 0;
        while(childrenCount + speciesList.size() < poolSize)
        {
            //make new child
            Species s;
            //TODO: select species to use for new stuff
            NeuralNetwork nn = newChild(s);
            nn.mutate();

            //add nn to new species
            childrenCount++;
        }

        generation++;
    }

    private void cullSpecies(boolean onlyBest)
    {
        for(Species s : speciesList)
        {
            NeuralNetwork[] genomes = s.sort();//sort genomes in species based on fitness value
            s.getGenomes().clear();
            double newSize = onlyBest ? 1 : Math.ceil(genomes.length/2.0);
            for (int i = 0; i < newSize; i++) {
                s.getGenomes().add(genomes[i]);
            }
        }
    }

    private NeuralNetwork newChild(Species s)
    {
        NeuralNetwork child;
        Random random = new Random();
        if(random.nextDouble() < crossoverChance)
        {
            //make new child using crossover between two existing genes
            NeuralNetwork nn1 = s.getGenomes().get(random.nextInt(s.getGenomes().size()));
            NeuralNetwork nn2 = s.getGenomes().get(random.nextInt(s.getGenomes().size()));

            child = NeuralNetwork.crossOver(nn1,nn2);
        }else
        {
            //make new child without crossover
            child = s.getGenomes().get(random.nextInt(s.getGenomes().size()));
        }

        child.mutate();

        return child;
    }
}
