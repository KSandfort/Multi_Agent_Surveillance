package agents.NeuralNetworkUtil.NEAT;

import agents.NeuralNetworkUtil.NeuralNetwork;

import java.util.ArrayList;
import java.util.Arrays;
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

    public static NeuralNetwork[] sort(NeuralNetwork[] networks)
    {
        //NeuralNetwork[] networks = genomes.toArray(new NeuralNetwork[0]);

        if(networks.length < 2)
        {
            return networks;//if length is 0 or 1 the network is already sorted
        }
        mergeSort(networks);
        //quickSort(networks,0,networks.length-1);

        return networks;
    }

    private static void mergeSort(NeuralNetwork[] arr)
    {
        if(arr.length <= 1)
            return;

        int middle = arr.length / 2;

        //make two arrays half the length of the original array
        NeuralNetwork[] left = new NeuralNetwork[middle];
        NeuralNetwork[] right = new NeuralNetwork[arr.length - middle];

        //copy values to the two new lists
        System.arraycopy(arr,0,left,0, middle);
        System.arraycopy(arr,middle,right,0, arr.length - middle);

        //sort both lists
        mergeSort(left);
        mergeSort(right);

        int iL = 0;//index of the left array
        int iR = 0;//index of the right array

        for (int i = 0; i < arr.length; i++) {
            if(iL >= left.length)
                arr[i] = right[iR++];
            else if(iR >= right.length)
                arr[i] = left[iL++];
            else if(left[iL].getFitness() >= right[iR].getFitness())
                arr[i] = left[iL++];
            else
                arr[i] = right[iR++];
        }


    }

    private NeuralNetwork[] quickSort(NeuralNetwork[] arr, int Left, int Right)
    {

        int l = Left;
        int r = Right;
        double middle = arr[(l + r)/2].getFitness();

        do
        {
            while(arr[l].getFitness() > middle)
            {
                l++;
            }
            while(middle > arr[r].getFitness())
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

    public void setGenomes(List<NeuralNetwork> genomes) {
        this.genomes = genomes;
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
