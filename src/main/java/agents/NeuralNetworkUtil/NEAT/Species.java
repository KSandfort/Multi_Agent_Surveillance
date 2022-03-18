package agents.NeuralNetworkUtil.NEAT;

import agents.NeuralNetworkUtil.NeuralNetwork;

import java.util.ArrayList;
import java.util.List;

public class Species
{
    List<NeuralNetwork> genomes;
    double topFitness;
    double staleness;
    double averageFitness;


    public Species()
    {
        genomes = new ArrayList<>();
        topFitness = 0;
        staleness = 0;
        averageFitness = 0;
    }

    public NeuralNetwork[] sort()
    {
        //TODO:implement sorting algorithm
        return null;
    }

    public List<NeuralNetwork> getGenomes() {
        return genomes;
    }
}
