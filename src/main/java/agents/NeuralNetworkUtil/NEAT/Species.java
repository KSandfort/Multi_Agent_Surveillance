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
        NeuralNetwork[] networks = genomes.toArray(new NeuralNetwork[0]);

        quickSort(networks,0,networks.length-1);

        return networks;
    }

    private NeuralNetwork[] quickSort(NeuralNetwork[] arr, int Left, int Right)
    {

        int l = Left;
        int r = Right;
        double middle = arr[(l + r)/2].getFitness();

        do
        {
            while(arr[l].getFitness() < middle)
            {
                l++;
            }
            while(middle < arr[r].getFitness())
            {
                r--;
            }
            if(l <= r)
            {
                NeuralNetwork temp = arr[l];
                arr[r] = arr[l];
                arr[l] = temp;
                l++;
                r--;
            }
        }while(l < r);

        if(l < Right)
        {
            arr = quickSort(arr,l,Right);
        }
        if(Left < r)
        {
            arr = quickSort(arr,Left,r);
        }

        return arr;
    }

    public List<NeuralNetwork> getGenomes() {
        return genomes;
    }

    public double getAverageFitness()
    {
        double average = 0;
        for(NeuralNetwork nn : genomes)
        {
            average += nn.getFitness();
        }
        average /= genomes.size();
        averageFitness = average;
        return averageFitness;
    }
}
