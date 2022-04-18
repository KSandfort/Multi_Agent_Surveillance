package agents.NeuralNetworkUtil.NEAT;

import agents.NEATAgent;
import agents.NeuralNetworkUtil.NeuralNetwork;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GenePool
{
    final double distanceThreshold = 2.0;
    final double crossoverChance = 0.75;
    final int maxStaleness = 20;
    final int poolSize = 200;//population size. eg how many genomes in a generation

    int input;
    int output;

    List<Species> speciesList;
    int generation;
    double maxFitness;
    public NeuralNetwork bestNetwork;

    public GenePool(int in, int out)
    {
        input = in;
        output = out;
        speciesList =  new ArrayList<>();
        generation = 0;
        maxFitness = 0;
    }

    public void init()
    {
        List<NeuralNetwork> children = new ArrayList<>();//new generation

        while(children.size() < poolSize)
        {
            //make new child
            NeuralNetwork init = new NeuralNetwork();
            init.init();
            children.add(init);
        }

        //add new genomes to species
        for(NeuralNetwork nn : children)
        {
            addToPool(nn);
        }
    }

    //TODO: speciation is probably not 100% optimal, this is pretty much copied from another project and deviates a bit from the papers
    //make new generation
    public void newGeneration()
    {
        //more debugging statements on lines: 186, 216, 252
        System.out.println("\nGeneration " + generation);

        //simulate and calculate fitness
        simulate();

        //new generation
        List<NeuralNetwork> children = new ArrayList<>();

        //rank species
        ranking();

        //remove bottom half of each species
        cullSpecies(false);

        System.out.println("Fitness of gen " + generation + ": " + maxFitness);
        System.out.println("Number of species before culling: " + speciesList.size());

        //remove stale species
        removeStale();

        //calculate average fitness
        double totalAvgFitness = calcTotalAvgFitness();

        //remove weak species
        removeWeak();

        //breed proportional to fitness value
        for(Species s : speciesList) {
            //children to be bred from this species
            double amount = Math.floor(s.averageFitness / totalAvgFitness * poolSize) - 1;
            for (int i = 0; i < amount; i++) {
                NeuralNetwork nn = newChild(s);
                children.add(nn);
            }
        }

        //cull all but top member of each species
        cullSpecies(true);

        System.out.println("Number of species after culling: " + speciesList.size());

        //add remaining population using top of species
        while(children.size() + speciesList.size() < poolSize)
        {
            //make new child
            Random random = new Random();
            Species s = speciesList.get(random.nextInt(speciesList.size()));
            NeuralNetwork nn = newChild(s);//TODO: this is wrong, crossover will select the same genome twice
            children.add(nn);
        }

        //add new genomes to species
        for(NeuralNetwork nn : children)
        {
            addToPool(nn);
        }

        generation++;
    }

    //
    private void ranking()
    {
        maxFitness = -1;
        bestNetwork = null;
        for(Species s : speciesList)
        {
            NeuralNetwork best = s.getGenomes().get(0);
            if(best.getFitness() > maxFitness)
            {
                maxFitness = best.getFitness();
                bestNetwork = best.copy();
            }
        }
    }

    //evaluate the fitness of the genomes, this method will have to change if you want to change the use of the network
    private void simulate()
    {
        for(Species s : speciesList)
        {
            for(NeuralNetwork nn : s.getGenomes())
            {
                double[] expected = {0,1,1,0};
                double res1 = nn.evaluate(new double[]{0, 0})[0];
                double res2 = nn.evaluate(new double[]{1, 0})[0];
                double res3 = nn.evaluate(new double[]{0, 1})[0];
                double res4 = nn.evaluate(new double[]{1, 1})[0];
                double[] result = {res1,res2,res3,res4};

                double error = 0;
                for (int i = 0; i < expected.length; i++) {
                    error += (expected[i] - result[i])*(expected[i] - result[i]);//squared error
                }
                error = 4 - error;//make lowest error highest fitness
                error *= error;//square error to make difference bigger

                nn.setFitness(error);
            }
        }
    }

    //adds a new child to the correct species, based on distance measure
    //or creates new species if it does not belong to any species
    private void addToPool(NeuralNetwork nn)
    {
        for(Species s : speciesList)
        {
            if(NeuralNetwork.distance(nn,s.getGenomes().get(0)) < distanceThreshold)
            {
                s.getGenomes().add(nn);
                return;
            }
        }

        Species newSpecies = new Species();
        newSpecies.getGenomes().add(nn);
        speciesList.add(newSpecies);
    }

    //removes any species that based on their average fitness are not supposed to produce any offspring
    //the number of offspring a species is allowed to create is the percentage of the species average fitness compared to the total average fitness of all the species
    private void removeWeak()
    {
        List<Species> survivingSpecies = new ArrayList<>();
        double totalAvgFitness = calcTotalAvgFitness();
        int counter =0;
        for(Species s : speciesList)
        {
            double amount = Math.floor(s.averageFitness / totalAvgFitness * poolSize);
            if(amount >= 1)
            {
                survivingSpecies.add(s);
            }else
            {
                //System.out.println("Removed weak species:" + counter);
            }
            counter++;
        }
        speciesList = survivingSpecies;
    }

    //remove any species that have not improved in a predefined amount of generations
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

    //returns the sum of average fitness of all the species
    private double calcTotalAvgFitness()
    {
        double output = 0;
        for(Species s : speciesList)
        {
            double totalFitness = 0;
            for(NeuralNetwork nn : s.getGenomes())
            {
                totalFitness += nn.getFitness();
            }
            s.averageFitness = totalFitness/s.getGenomes().size();

            output += s.getAverageFitness();
        }
        return output;
    }

    //removes the least fit genomes in the species
    private void cullSpecies(boolean onlyBest)
    {
        for(Species s : speciesList)
        {
            NeuralNetwork[] genomes = s.sort();//sort genomes in species based on fitness value
            s.getGenomes().clear();

            double newSize = onlyBest ? 1 : genomes.length;//Math.ceil(genomes.length/2.0);
            for (int i = 0; i < newSize; i++) {
                s.getGenomes().add(genomes[i]);
            }
            //System.out.println("Species best fitness: " + s.topFitness);
        }
    }

    //create a new child using a species, there is a 75% chance a new child is the result of a crossover
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
            child = s.getGenomes().get(random.nextInt(s.getGenomes().size())).copy();
        }

        child.mutate();

        return child;
    }
}
