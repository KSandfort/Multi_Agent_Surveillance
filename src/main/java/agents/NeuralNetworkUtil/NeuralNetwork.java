package agents.NeuralNetworkUtil;

import java.util.*;

public class NeuralNetwork {
    //coefficients for compatability distance calculation
    final static double c1 =1;
    final static double c2 =1;
    final static double c3 =0.4;

    final double wMutationP = 0.8;
    final double wResetP = 0.9;
    
    List<NNNode> nodes;
    int maxNeurons = 0;
    private List<NNConnection> connections;
    double fitness;
    int inputNum;
    int outputNum;
    Random random;
    public NeuralNetwork(int in, int out)
    {
        inputNum = in;
        outputNum = out;
        maxNeurons = in + out;
        nodes = new ArrayList<NNNode>();
        connections = new ArrayList<NNConnection>();
        random = new Random();
    }
    //calculate neural network
    public double[] evaluate(double[] input)
    {
        double[] output = new double[outputNum];
        
        
        
        return null;
    }

    public static NeuralNetwork crossOver(NeuralNetwork first, NeuralNetwork second)
    {
        NeuralNetwork a;
        NeuralNetwork b;
        //always make sure a is the fitter network
        if(first.getFitness() < second.getFitness())
        {
            a = first;
            b = second;
        }else
        {
            a = second;
            b = first;
        }

        NeuralNetwork nn = new NeuralNetwork(a.getInputNum(),a.getOutputNum());
        List<NNConnection> aConn = a.getConnections();
        List<NNConnection> bConn = b.getConnections();
        List<NNConnection> newConnections = new ArrayList<NNConnection>();

        //indices for connections of both network to be able to iterate over both lists;
        int i = 0;
        int k = 0;
        Random random = new Random();
        while(i < aConn.size() || k < bConn.size())
        {
            NNConnection aC;
            NNConnection bC;
            int aCount;//innovation count of 'a' connection
            int bCount;//innovation count of 'b' connection
            try {
                aC = aConn.get(i);
                aCount = aC.getInnovationCount();
            }catch (Exception e)
            {
                break;
            }

            try {
                bC = bConn.get(k);
                bCount = bC.getInnovationCount();
            }catch (Exception e)
            {
                bC = null;
                bCount = Integer.MAX_VALUE;
            }

            if(aCount == bCount)
            {
                System.out.println(aCount +"-"+ bCount);
                if(random.nextBoolean())//if genes match randomly select one of the two genes
                {
                    System.out.println("Selecting a");
                    newConnections.add(aC.copy());
                }else
                {
                    System.out.println("Selecting b");

                    newConnections.add(bC.copy());
                }
                i++;
                k++;
            }
            else if(aCount < bCount)
            {
                
                newConnections.add(aC.copy());//only copy excess/disjoint genes from fittest genome
                i++;
            }
            else
            {
                k++;
            }
        }

        nn.setConnections(newConnections);
        return nn;
    }
    
    public static double distance(NeuralNetwork a, NeuralNetwork b)
    {
        int i = 0;
        int k = 0;

        List<NNConnection> aConn = a.getConnections();
        List<NNConnection> bConn = b.getConnections();

        int e = 0;//number of excess genes
        int d = 0;//number of disjoint genes
        int m = 0;//number of matching genes
        int dW = 0;// total difference in weight between matching genes
        boolean isExcess = false;

        while(i < aConn.size() || k < bConn.size())
        {
            int aIn;
            int bIn;
            try{
                aIn = aConn.get(i).getInnovationCount();
            }catch(Exception ex)
            {
                isExcess = true;
                aIn = Integer.MAX_VALUE;
            }
            
            try{
                bIn = bConn.get(k).getInnovationCount();
            }catch(Exception ex)
            {
                isExcess = true;
                bIn = Integer.MAX_VALUE;
            }

            if(aIn == bIn)
            {
                dW += Math.abs(aConn.get(i).getWeight() + bConn.get(k).getWeight());
                m++;
                i++;
                k++;
                continue;
            }
            if(isExcess)
            {
                e++;
            }else
            {
                d++;
            }
            if(aIn < bIn)
            {
                i++;
            }
            else
            {
                k++;
            }
        }

        int n = Math.max(aConn.size(), bConn.size());



        return c1*e/n + c2*d/n + c3*dW/m;
    }
    public void mutateWeights(){
        
        
        for (NNConnection c : connections) {
            if(random.nextDouble() > wMutationP)
                continue;
            if(random.nextDouble() > wResetP)
            {
                c.resetWeight();
            }else
            {
                c.permuteWeight();
            }
        }
    }

    public void addNode()
    {
        if(connections.size() == 0)
            return;

        NNConnection gene = connections.get(random.nextInt(connections.size()));

        if(!gene.isEnabled())
            return;

        gene.setEnabled(false);

        maxNeurons++;
        NNConnection newConn1 = new NNConnection(gene.getIn(), maxNeurons, 1, true);
        NNConnection newConn2 = new NNConnection(maxNeurons,gene.getOut(), gene.getWeight(), true);
        connections.add(newConn1);
        connections.add(newConn2);
    }

    public void addConnection()
    {
        int neuron1 = randomNeuron(true);
        int neuron2 = randomNeuron(false);

        //check if connection exist in current genome
        for (NNConnection c : connections) {
            if(c.getIn() == neuron1 && c.getOut() == neuron2)
                return;
        }

        //TODO: maybe add check to prevent two same structures having different innovation numbers, also needs to be added to other mutations

        NNConnection newConn = new NNConnection(neuron1, neuron2, 1, true);
        newConn.resetWeight();

        connections.add(newConn);
    }

    public int randomNeuron(boolean canBeInput)
    {
        //default value of boolean is false so we dont need to initialize the values of the array, since we want them to be false
        boolean[] possibleNeurons = new boolean[maxNeurons];

        //either include the input neurons in the selectable neurons or the oputput neurons to make it possible to select two neurons without selecting two output or input neurons
        if(canBeInput)
        {
            for (int i = 0; i < inputNum; i++) {
                possibleNeurons[i] = true;
            }
        }else
        {
            for (int i = 0; i < outputNum; i++) {
                possibleNeurons[inputNum + i] = true;
            }
        }

        for (int i = inputNum + outputNum; i < maxNeurons; i++) {//add hidden neurons
            possibleNeurons[i] = true;
        }


        for (NNConnection c : connections) {
            if(canBeInput && (c.getIn() < inputNum || c.getIn() >= inputNum + outputNum) )
            {
                possibleNeurons[c.getIn()] = true;
            }
            if(!canBeInput || c.getOut() >= inputNum)
            {
                possibleNeurons[c.getOut()] = true;
            }
        }
        
        //select two random neuron
        int n;//randomly selected neuron
        do
        {
            n = random.nextInt(maxNeurons);
        }
        while(!possibleNeurons[n]);

        return n;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public List<NNConnection> getConnections() {
        return connections;
    }

    public void setConnections(List<NNConnection> connections) {
        this.connections = connections;
    }


    public int getInputNum() {
        return inputNum;
    }

    public int getOutputNum() {
        return outputNum;
    }
    @Override
    public String toString() {
        String output = "NeuralNetwork : ";
        List<NNConnection> conn = getConnections();
        for (NNConnection c : conn) {
            output += c.toString();
        }
        return output;
    }
}
