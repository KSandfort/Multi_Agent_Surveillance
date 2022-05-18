package model.neural_network;

import agents.NeatAgent;
import controller.GameController;
import gui.SimulationGUI;
import utils.RunSimulation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class  GenePool
{
    final double distanceThreshold = 2.0;
    final double crossoverChance = 0.75;
    final int maxStaleness = 20;
    final int poolSize = 100;//population size. eg how many genomes in a generation
    final int maxSteps = 2000;//maximum amount of steps to run the simulation for


    int input;
    int output;

    List<Species> speciesList;
    List<NeuralNetwork> pool;
    int generation;
    double maxFitness;
    public NeuralNetwork bestNetwork;

    public GenePool(int in, int out)
    {
        input = in;
        output = out;
        speciesList =  new ArrayList<>();
        pool = new ArrayList<>();
        generation = 0;
        maxFitness = 0;
    }

    public void init(boolean readFromFile)
    {
        if(readFromFile)
            NeuralNetwork.readGlobals("fromPreviousSim.txt");

        while(pool.size() < poolSize)
        {
            NeuralNetwork init;
            //make new child
            if(!readFromFile) {
                init = new NeuralNetwork();
                init.init();
            }
            else{
                init = NeuralNetwork.readNetwork("bestNetwork.txt");
                init.mutate();
            }
            pool.add(init);
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

        //add all to species
        speciate();

        //sort and rank species
        ranking();

        System.out.println("Best fitness " + maxFitness);
        //System.out.println("First specie: " + speciesList.get(0));
        //best off species with >= 5 genomes move to next generation unchanged
        List<NeuralNetwork> bestFromPreviousGen = bestFromPreviousGen();

        //System.out.println(Arrays.toString(bestFromPreviousGen.toArray()));

        //remove bottom half of each species
        List<NeuralNetwork> survivingGenomes = cullSpecies(false);

        //calculate adjusted fitness value using fitness sharing
        fitnessSharing();

        //create new generation
        createNewGen(bestFromPreviousGen,survivingGenomes);

        generation++;
    }

    private void createNewGen(List<NeuralNetwork> fromPrevious, List<NeuralNetwork> surviving) {
        pool.clear();
        pool.addAll(fromPrevious);
        NeuralNetwork[] oldPool = Species.sort(surviving.toArray(new NeuralNetwork[0]));

        //create new offspring
        while (pool.size() < poolSize)
        {
            //select two new parents for crossover
            if(new Random().nextDouble() <= crossoverChance)
            {
                NeuralNetwork newChild = NeuralNetwork.crossOver(newParent(oldPool),newParent(oldPool));
                newChild.mutate();
                pool.add(newChild);
            }else
            {
                NeuralNetwork newChild = newParent(oldPool).copy();
                newChild.mutate();
                pool.add(newChild);
            }
        }
    }

    private NeuralNetwork newParent(NeuralNetwork[] oldPool)
    {
        double totalFitness = totalFitness(oldPool);
        double selected = new Random().nextDouble() * totalFitness;

        for(NeuralNetwork nn : oldPool)
        {
            if(nn.getFitness() >=  selected)
            {
                return nn;
            }else
            {
                selected -= nn.getFitness();
            }
        }
        //no parent found, should technically not be possible I think
        return oldPool[oldPool.length - 1];
    }

    private double totalFitness(NeuralNetwork[] pool)
    {
        double fitness = 0;
        for(NeuralNetwork nn : pool)
        {
            fitness += nn.getFitness();
        }
        return fitness;
    }

    private List<NeuralNetwork> bestFromPreviousGen() {
        List<NeuralNetwork> toReturn = new ArrayList<NeuralNetwork>();
        for(Species s : speciesList)
        {
            if(s.getGenomes().size() >= 5)
            {
                toReturn.add(s.getGenomes().get(0).copy());
            }
        }
        toReturn.add(bestNetwork.copy());
        NeuralNetwork mutatedBest = bestNetwork.copy();
        mutatedBest.mutate();
        toReturn.add(mutatedBest);
        return toReturn;
    }

    private void speciate() {
        speciesList.clear();
        //add new genomes to species
        for(NeuralNetwork nn : pool)
        {
            addToPool(nn);
        }
    }

    private void fitnessSharing() {
        for(NeuralNetwork nn : pool)
        {
            nn.setFitness(computeAdjustedFitnessValue(nn));
        }
    }

    private double computeAdjustedFitnessValue(NeuralNetwork nn) {
        int neighbourhood = 0;//number of genomes within distance threshold

        for(NeuralNetwork nn2 : pool)
        {
            double dist;
            if(nn2.getConnections().size() > nn.getConnections().size())
                dist = NeuralNetwork.distance(nn2,nn);
            else
                dist = NeuralNetwork.distance(nn,nn2);
            if(dist < distanceThreshold)
            {
                neighbourhood++;
            }
        }

        if(neighbourhood == 0)
        {
            System.out.println("AHHHH how is this even possible!!!!!");
        }

        return nn.getFitness()/neighbourhood;
    }

    //
    private void ranking()
    {
        maxFitness = -1;
        bestNetwork = null;
        for(Species s : speciesList)
        {
            NeuralNetwork[] sortedNetworks = Species.sort(s.getGenomes().toArray(new NeuralNetwork[0]));
            List<NeuralNetwork> networks = new ArrayList<NeuralNetwork>(Arrays.asList(sortedNetworks));
            s.setGenomes(networks);
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
        for(NeuralNetwork nn : pool)
        {
            nn.setFitness(runSim(nn));
        }
    }

    private double runSim(NeuralNetwork nn)
    {
        NeatAgent.setNn(nn);//set neural network to be used by the agent
        GameController.guardAgentType = NNTraining.guardType;//use the neat agent
        GameController.intruderAgentType = NNTraining.intruderType;
        GameController.terminalFeedback = false;
        SimulationGUI.bypassPath = NNTraining.mapPath;
        GameController result = GameController.simulate(maxSteps,3,3,3,1);

        return NNTraining.trainGuard ? result.getFitnessGuards() : result.getFitnessIntruders();
    }

    //adds a new child to the correct species, based on distance measure
    //or creates new species if it does not belong to any species
    private void addToPool(NeuralNetwork nn)
    {
        for(Species s : speciesList)
        {
            double dist;
            NeuralNetwork rep = s.getGenomes().get(0);
            if(nn.getConnections().size() > rep.getConnections().size())
                dist = NeuralNetwork.distance(nn,rep);
            else
                dist = NeuralNetwork.distance(rep,nn);
            if(dist < distanceThreshold)
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
    private List<NeuralNetwork> cullSpecies(boolean onlyBest)
    {
        for(Species s : speciesList)
        {
            NeuralNetwork[] genomes = s.getGenomes().toArray(new NeuralNetwork[0]);//sort genomes in species based on fitness value
            s.getGenomes().clear();

            double newSize = onlyBest ? 1 : Math.ceil(genomes.length/2.0);
            for (int i = 0; i < newSize; i++) {
                s.getGenomes().add(genomes[i]);
            }
            //System.out.println("Species best fitness: " + s.topFitness);
        }
        List<NeuralNetwork> surviving = new ArrayList<NeuralNetwork>();
        for(Species s : speciesList)
        {
            surviving.addAll(s.getGenomes());
        }

        return surviving;
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
