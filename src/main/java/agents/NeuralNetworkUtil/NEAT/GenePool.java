package agents.NeuralNetworkUtil.NEAT;

import agents.NeuralNetworkUtil.NeuralNetwork;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GenePool
{
    final double distanceThreshold = 3.0;
    final double crossoverChance = 0.75;
    final int maxStaleness = 15;
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
        List<NeuralNetwork> children = new ArrayList<>();//new generation
        cullSpecies(false);//remove bottom half of each species

        //remove stale species
        removeStale();

        //calculate average fitness
        double totalAvgFitness = calcTotalAvgFitness();

        //remove weak species

        //breed proportional to fitness value
        for(Species s : speciesList)
        {
            //children to be bred from this species
            double amount = Math.floor(s.averageFitness / totalAvgFitness * poolSize) - 1;
            for (int i = 0; i < amount; i++) {
                NeuralNetwork nn = newChild(s);
                children.add(nn);
            }
        }


        //cull all but top member of each species
        cullSpecies(true);

        //add remaining population using top of species

        while(children.size() + speciesList.size() < poolSize)
        {
            //make new child
            Random random = new Random();
            Species s = speciesList.get(random.nextInt(speciesList.size()));
            NeuralNetwork nn = newChild(s);
            children.add(nn);
        }

        //add new genomes to species
        for(NeuralNetwork nn : children)
        {
            //TODO: add new children to species
        }

        generation++;
    }

    private void removeStale()
    {
        List<Species> survivingSpecies = new ArrayList<>();

        for (int i = 0; i < speciesList.size(); i++) {
            Species s = speciesList.get(i);

            if(s.topFitness < s.getGenomes().get(0).getFitness())
            {
                s.topFitness = s.getGenomes().get(0).getFitness();
                s.staleness = 0;
            }
            else
            {
                s.staleness++;
            }

            if(s.staleness < maxStaleness){
                survivingSpecies.add(s);
            }
        }

        speciesList = survivingSpecies;
    }

    private double calcTotalAvgFitness()
    {
        double output = 0;
        for(Species s : speciesList)
        {
            output += s.getAverageFitness();
        }
        return output;
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
